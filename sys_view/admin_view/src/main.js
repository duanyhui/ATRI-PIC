import Vue from 'vue'
import ElementUI from 'element-ui';
// import VForm from 'vform-builds'  //引入VForm库

import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";

//引入Day.js
import dayjs from "dayjs";
//挂载在vue上
Vue.prototype.dayjs=dayjs

// Vue.config.productionTip = false
Vue.use(ElementUI);
// Vue.use(VForm)  //全局注册VForm(同时注册了v-form-designer和v-form-render组件)

import Tui from "@wocwin/t-ui"

Vue.use(Tui)
// axios.defaults.baseURL = 'https://jd.atri.wiki';
axios.defaults.baseURL = 'http://localhost:8082';
// 添加请求拦截器，在请求头中加token
axios.interceptors.request.use(
    config => {
      if (sessionStorage.getItem('satoken')) {
        config.headers.satoken = sessionStorage.getItem('satoken');

      }
      return config;
    },
);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')



