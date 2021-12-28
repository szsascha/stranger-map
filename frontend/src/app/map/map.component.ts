import { Component, OnInit } from "@angular/core";
import Map from "ol/Map";
import View from "ol/View";
import OSM from "ol/source/OSM";
import * as olProj from "ol/proj";
import TileLayer from "ol/layer/Tile";
import { PositionService } from "../position.service";
import { Overlay } from "ol";
import {fromLonLat} from 'ol/proj';
import { ApiService } from "../api.service";
import { switchMap, withLatestFrom } from "rxjs/operators";

@Component({
  selector: "app-map",
  templateUrl: "./map.component.html",
  styleUrls: ["./map.component.scss"],
})
export class MapComponent implements OnInit {
  map: Map | undefined;
  selfMarker: any;


  constructor(private positionService: PositionService, private apiService: ApiService) {

    this.apiService.session({name: 'Enrico', description: 'Gibt mir Geld!'}).subscribe(x => {

      const uuid = x.uuid;
      this.positionService.position$
        .subscribe((position) => {
        console.log(position!.coords);
  
        var element = document.createElement("div");
        element.innerHTML = '<img src="https://cdn.mapmarker.io/api/v1/fa/stack?size=50&color=DC4C3F&icon=fa-microchip&hoffset=1" />';
  
        const pos = fromLonLat([position!.coords.longitude,position!.coords.latitude])
        if(this.selfMarker){
          this.map?.removeOverlay(this.selfMarker);
        }
        this.selfMarker = new Overlay({
          position: pos,
          positioning: "center-center",
          element: element,
          stopEvent: false,
        });
        this.map?.addOverlay(this.selfMarker);

        this.apiService.peers({uuid: uuid, lat: position!.coords.latitude, lon: position!.coords.longitude}).subscribe(x => {
          console.log(x)
        })
        
      });
    });
    
  }

  ngOnInit(): void {
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
        zoom: 5,
      }),
    });
  }
}
