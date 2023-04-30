import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import test from "@/views/Home.vue";
import ImageDetail from "@/components/ImageDetail.vue";
import SearchByTag from "@/views/SearchByTag.vue";
import Home from "@/views/Home.vue";

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
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
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

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
