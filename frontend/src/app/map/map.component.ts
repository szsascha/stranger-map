import { Component, OnInit } from "@angular/core";
import Map from "ol/Map";
import View from "ol/View";
import OSM from "ol/source/OSM";
import * as olProj from "ol/proj";
import TileLayer from "ol/layer/Tile";
import { PositionService } from "../position.service";
import { ApiService } from "../api.service";
import { ActivatedRoute } from "@angular/router";
import { map, switchMap, take } from "rxjs";
import { Overlay } from "ol";

@Component({
  selector: "app-map",
  templateUrl: "./map.component.html",
  styleUrls: ["./map.component.scss"],
})
export class MapComponent implements OnInit {
  map: Map | undefined;
  markers: any[] = [];

  constructor(
    private positionService: PositionService,
    private apiService: ApiService,
    private activatedRoute: ActivatedRoute,
  ) {}

  private initMapPosition(position: any) {
    this.map?.getView().setCenter(
      olProj.fromLonLat([position.coords.longitude, position.coords.latitude]),
    );
    this.map?.getView().setZoom(15);
  }

  private createMarker(position: any, isPeer: boolean = false) {
    const element = document.createElement("div");
    if(isPeer) {
      element.innerHTML =
      '<img src="https://cdn.mapmarker.io/api/v1/fa/stack?size=25&color=00ff00&icon=fa-microchip&hoffset=1" />';
    } else {
      element.innerHTML =
      '<img src="https://cdn.mapmarker.io/api/v1/fa/stack?size=50&color=DC4C3F&icon=fa-microchip&hoffset=1" />';
    }
    

    const pos = olProj.fromLonLat([
      position!.coords.longitude,
      position!.coords.latitude,
    ]);

    const marker = new Overlay({
      position: pos,
      positioning: "center-center",
      element: element,
      stopEvent: false,
    });

    return marker;
  }

  private resetMarkers() {
    this.markers?.forEach(pm => {
      this.map?.removeOverlay(pm);
    })
    this.markers = [];
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(
      take(1),
      map((x) => x["uuid"]),
      switchMap((uuid) => {
        return this.positionService.position$.pipe(
          map((position, k) => {
            this.resetMarkers();

            if (k === 0) {
              this.initMapPosition(position);
            }

            const marker = this.createMarker(position)
            this.markers.push(marker);
            this.map?.addOverlay(marker);
            return position;
          }),
          switchMap(position => {
            return this.apiService.peers({uuid: uuid, lat: position!.coords.latitude, lon: position!.coords.longitude})
          }),
          map((peersDto) => {
            console.log(peersDto.peers);
            const markers = peersDto.peers.map(x => {
              const m = this.createMarker({coords:{longitude:x.lon,latitude:x.lat}}, true)
              return m
            })

            markers.forEach(m => {
              this.markers.push(m);
              this.map?.addOverlay(m);
            })
          })
        );
      }),
    ).subscribe();

    this.map = new Map({
      controls: [],
      target: "stranger-map",
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
      ],
      view: new View({
        center: olProj.fromLonLat([7.0785, 51.4614]),
        zoom: 1,
      }),
    });
  }
}
