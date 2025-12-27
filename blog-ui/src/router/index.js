import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { path: "/login", component: () => import("../views/Login.vue") },
  {
    path: "/",
    component: () => import("../views/Layout.vue"), // 布局页，含导航栏
    children: [
      { path: "", component: () => import("../views/Home.vue") }, // 首页文章列表
      {
        path: "article/:id",
        component: () => import("../views/ArticleDetail.vue"),
      }, // 详情
      {
        path: "user/articles",
        component: () => import("../views/UserArticles.vue"),
      }, // 个人文章管理
      {
        path: "admin/audit",
        component: () => import("../views/AdminAudit.vue"),
      }, // 管理员审核
      {
        path: "admin/categories",
        component: () => import("../views/AdminCategories.vue"),
      }, // 分类管理
      {
        path: "admin/stats",
        component: () => import("../views/AdminStats.vue"),
      }, // Echarts统计
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
