import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@fortawesome/fontawesome-free/css/all.css'




Vue.component('font-awesome-icon');
Vue.use(ElementUI);
Vue.config.productionTip = false

axios.defaults.baseURL = 'http://atri.wiki:2082';
// 添加请求拦截器，在请求头中加token
axios.interceptors.request.use(
  config => {
    if (localStorage.getItem('satoken')) {
      config.headers.satoken = localStorage.getItem('satoken');

    }
    return config;
  },
);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
