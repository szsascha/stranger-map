import 'material-design-icons-iconfont/dist/material-design-icons.css'
import 'vuelayers/lib/style.css'

import Vue from 'vue'
import VueRouter from 'vue-router'
import VueLayers from 'vuelayers'

import App from './App.vue'
import vuetify from './plugins/vuetify'
import Join from './components/Join'
import MainMap from './components/MainMap'

Vue.config.productionTip = false

Vue.use(VueRouter);
Vue.use(VueLayers);

Vue.use(vuetify, {
  theme: {
     primary: '#7957d5',
  },
});

const router = new VueRouter({
  routes: [
     {
        path: '/join',
        component: Join,
     },
     {
        path: '/map',
        component: MainMap,
     }
  ],
});

new Vue({
  vuetify,
  router: router,
  render: h => h(App)
}).$mount('#app');

router.replace('/join');