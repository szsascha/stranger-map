<template>
  <div style="height: calc(100vh - 64px)">
    <vl-map :default-controls="controls" :default-interactions="false" :load-tiles-while-animating="true" :load-tiles-while-interacting="true"
             data-projection="EPSG:4326">
      <vl-view :zoom.sync="zoom" :center.sync="geolocPosition" :rotation.sync="rotation"></vl-view>

      <vl-geoloc @update:position="geolocPosition = $event">
        <template slot-scope="geoloc">
          <vl-feature v-if="geoloc.position" id="position-feature">
            <vl-geom-point :coordinates="geoloc.position"></vl-geom-point>
            <vl-style-box>
              <vl-style-icon src="https://openlayers.org/en/latest/examples/data/icon.png" :scale="1.0" :anchor="[0.5, 1]"></vl-style-icon>
            </vl-style-box>
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
        controls: controls
      }
    },
}
</script>