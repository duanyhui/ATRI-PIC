import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import test from "@/views/Home.vue";
import ImageDetail from "@/components/ImageDetail.vue";
import SearchByTag from "@/views/SearchByTag.vue";
import Home from "@/views/Home.vue";
import Submit from "@/components/Submit.vue";
import About from "@/components/About.vue";

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },


  {
    path: '/art/:pid',
    name: 'ImageDetail',
    component: ImageDetail,
    props: true,
    cache: true // 添加缓存属性
  },
  {
    path: '/',
    name: 'test',
    component: Home
  },
  {
    path: "/search/:tag",
    name: "search",
    component: SearchByTag,
  },
  {
    path: '/submit',
    name: 'Submit',
    component: Submit
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  //其他地址重定向到首页
  {
    path: '*',
    redirect: '/'

  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
