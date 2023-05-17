import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import login from "@/login/index.vue";
import adminIndex from "@/views/adminIndex.vue";
import BaseLayOut from "@/components/BaseLayOut.vue";
import CheckPicList from "@/views/CheckPicList.vue";
import AllPicList from "@/views/AllPicList.vue";
import Blank from "@/components/Blank.vue";
import UserInfoList from "@/views/UserInfoList.vue";
import LogList from "@/views/LogList.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'login',
    component: login
  },
  {
    path: '/home',
    name: 'home',
    component:BaseLayOut,
    children:[
        {
            path: '/pic',
            name: '操作',
          component: Blank,
            children:[
                {
                    path: '/pic/check',
                    name: '审核',
                    component: CheckPicList,
                },
                {
                    path: '/pic/list',
                    name: '图片列表',
                    component: AllPicList,
                }
                ]
        },
        {
            path: '/log',
            name: '日志',
            component: Blank,
            children: [
                {
                    path: '/log/user',
                    name: '用户日志',
                    component: UserInfoList,
                },
                {
                    path: '/log/log',
                    name: '操作日志',
                    component: LogList,
                }
            ]
        }
        ],

  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
