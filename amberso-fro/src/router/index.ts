import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import IndexPage from "../pages/IndexPage.vue";

//路由--用户输入url之后，可以访问到对应的页面
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: IndexPage,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
