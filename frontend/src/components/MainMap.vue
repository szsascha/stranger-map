<template>
  <div style="height: calc(100vh - 64px)">
    <vl-map ref="map" :default-controls="controls" :default-interactions="false" :load-tiles-while-animating="true" :load-tiles-while-interacting="true"
            @click="onMapClick" data-projection="EPSG:4326">
      <vl-view :zoom.sync="zoom" :center.sync="geolocPosition" :rotation.sync="rotation"></vl-view>

      <vl-geoloc @update:position="geolocPosition = $event">
        <template slot-scope="geoloc">
          <vl-feature v-if="geoloc.position" id="peer4711">
            <vl-geom-point :coordinates="geoloc.position"></vl-geom-point>
            <vl-style-box>
                <vl-style-text text="😀" font="3.0em sans-serif">x</vl-style-text>
            </vl-style-box>

            <vl-overlay v-show="descriptions.peer4711description" :offset="overlayOffset" :position="geoloc.position">
                <template slot-scope="scope">
                    <v-card hover>
                        <v-card-title>
                            <h2>Description</h2>
                        </v-card-title>
                        
                        <v-card-text>
                            line 1<br>
                            line 2<br>
                            {{ scope.position }}
                        </v-card-text>
                        
                        <v-card-actions>
                            <v-btn color=success>Click #1</v-btn>
                            <v-btn text color=primary>Click #2</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn icon><v-icon>bookmark</v-icon></v-btn>
                        </v-card-actions>
                    </v-card>
                </template>
            </vl-overlay>
          </vl-feature>
        </template>
      </vl-geoloc>

      <vl-layer-tile id="osm">
        <vl-source-osm></vl-source-osm>
      </vl-layer-tile>
    </vl-map>
    <div style="padding: 20px">
      Zoom: {{ zoom }}<br>
      Rotation: {{ rotation }}<br>
      My geolocation: {{ geolocPosition }}
    </div>
  </div>
</template>

<script>

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
        showDescription: false,
        descriptions: {
            peer4711description: false
        }
      }
    },
    methods: {
        onMapClick (event) {
            let features = this.$refs.map.$map.getFeaturesAtPixel(event.pixel)
            Object.entries(this.descriptions).forEach(entry => {
                const key = entry[0];
                this.descriptions[key] = false;
            });

            if (features !== null && features.length > 0) {
                let element = features[0].getId() + 'description';
                this.descriptions[element] = true;
            }
        }
    }
}
</script>