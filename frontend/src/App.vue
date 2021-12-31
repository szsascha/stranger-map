<template>
   <div id="app"></div>
</template>
<template>
  <v-app id="app">
    <v-content style="overflow:auto">
      <template v-if="!$route.path.includes('join')"> 
        <v-app-bar flat dark color="primary">
          <v-toolbar-title>Stranger Map</v-toolbar-title>
        </v-app-bar>
      </template>
      <keep-alive :include="['Join']">
        <router-view></router-view>
      </keep-alive>
    </v-content>
  </v-app>
</template>

<script>
export default {
   data: () => ({
      drawer: false,
   }),
   props: {
      source: String,
   },
};
</script>

<script>
export default {
   name: 'App',
   data() {
      return {
         drawer: false,
      };
   },
};
</script>

<style></style>
