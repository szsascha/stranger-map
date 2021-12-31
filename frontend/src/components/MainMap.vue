<template>
  <div style="height: calc(100vh - 64px)">
    <vl-map ref="map" :default-controls="controls" :default-interactions="false" :load-tiles-while-animating="true" :load-tiles-while-interacting="true"
            @click="onMapClick" data-projection="EPSG:4326">
      <vl-view :zoom.sync="zoom" :center.sync="geolocPosition" :rotation.sync="rotation"></vl-view>

    <template :id="'peer-' + index" v-for="(peer, index) in peers">
        <vl-feature :key="index" :id="index">
            <vl-geom-point :coordinates="[peer.lon, peer.lat]"></vl-geom-point>
            <vl-style-box>
                <vl-style-text text="😀" font="3.0em sans-serif"></vl-style-text>
            </vl-style-box>

            <vl-overlay v-show="peer.showDescription" :offset="overlayOffset" :position="[peer.lon, peer.lat]">
                <template>
                    <v-card hover>                       
                        <v-card-text>
                            {{ peer.description }}
                        </v-card-text>
                        
                        <!--<v-card-actions>
                            <v-btn color=success>Click #1</v-btn>
                            <v-btn text color=primary>Click #2</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn icon><v-icon>bookmark</v-icon></v-btn>
                        </v-card-actions>-->
                    </v-card>
                </template>
            </vl-overlay>
          </vl-feature>
    </template>

    <!-- own peer -->
      <vl-geoloc @update:position="updatePosition">
        <template slot-scope="geoloc">
          <vl-feature v-if="geoloc.position" id="peer4711">
            <vl-geom-point :coordinates="geoloc.position"></vl-geom-point>
            <vl-style-box>
                <vl-style-text text="😀" font="3.0em sans-serif"></vl-style-text>
            </vl-style-box>
          </vl-feature>
        </template>
      </vl-geoloc>

      <vl-layer-tile id="osm">
        <vl-source-osm></vl-source-osm>
      </vl-layer-tile>
    </vl-map>
  </div>
</template>

<script>

import Vue from 'vue'

const controls = {
  zoom: false,
  attribution: false,
  rotate: false
}

export default {
   data () {
      return { 
        zoom: 18,
        rotation: 0,
        geolocPosition: undefined,
        controls: controls,
        overlayOffset: [25, -25],
        peers: []
      }
    },
    methods: {
        onMapClick (event) {
            let features = this.$refs.map.$map.getFeaturesAtPixel(event.pixel)
            this.peers.forEach((peer) => {
                peer.showDescription = false;
            });

            if (features !== null && features.length > 0) {
                this.peers[features[0].getId()].showDescription = true;
            }
        },
        updatePosition (event) {
            this.geolocPosition = event
            console.log(event);

            fetch("http://localhost:8081/api/peers", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    uuid: Vue.prototype.uuid,
                    lat: event[1],
                    lon: event[0]
                })
            })
            .then(response => { 
                if(response.ok){
                    return response.json();
                } else{
                    console.err("Server returned " + response.status + " : " + response.statusText);
                }                
            })
            .then(response => {
                response.peers.forEach(peer => {
                    peer.showDescription = false;
                });
                console.log(response.peers);
                this.peers = response.peers;
            })
            .catch(err => {
                console.log(err);
            });
        }
    }
}
</script>