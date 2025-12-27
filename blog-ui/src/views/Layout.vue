<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="header-inner">
        <!-- 博主信息悬浮卡片 -->
        <el-popover placement="bottom-start" :width="240" trigger="hover">
          <template #reference>
            <div class="logo">Doom Blog</div>
          </template>
          <div class="author-popover">
            <el-avatar
              :size="60"
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
            <h3>Admin</h3>
            <p>梦想是写出最优雅的代码。</p>
            <el-divider style="margin: 12px 0" />
            <div class="pop-stats">
              <span><b>12</b> 文章</span>
              <span><b>4</b> 分类</span>
            </div>
          </div>
        </el-popover>
      </div>
      <div class="flex-grow"></div>
      <el-menu mode="horizontal" :ellipsis="false" router>
        <el-menu-item index="/">首页</el-menu-item>

        <!-- 用户登录后才显示的菜单 -->
        <template v-if="username">
          <el-menu-item index="/user/articles">我的文章</el-menu-item>

          <!-- 仅管理员可见 -->
          <el-sub-menu v-if="role === 'ADMIN'" index="admin">
            <template #title>管理后台</template>
            <el-menu-item index="/admin/audit">文章审核</el-menu-item>
            <el-menu-item index="/admin/categories">类别管理</el-menu-item>
            <el-menu-item index="/admin/stats">数据统计</el-menu-item>
          </el-sub-menu>
        </template>

        <el-menu-item v-if="!username" index="/login">登录</el-menu-item>
        <el-sub-menu v-else index="user-info">
          <template #title>{{ nickname || username }}</template>
          <el-menu-item @click="handleLogout">退出登录</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>

    <el-main class="main">
      <router-view />
      <!-- 这里渲染子页面 -->
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import request from "../utils/request";

const router = useRouter();
const username = ref(localStorage.getItem("username"));
const role = ref(localStorage.getItem("role"));
const nickname = ref(localStorage.getItem("nickname"));

const handleLogout = async () => {
  try {
    // 即使后端 logout 接口没写逻辑，调一下也是好的
    await request.post("/auth/logout");
  } finally {
    localStorage.clear();
    ElMessage.success("退出成功");
    router.push("/login"); // 跳转到登录页
  }
};
</script>

<style>
/* 全局样式，用于覆盖Element Plus默认样式 */
.layout-container {
  min-height: 100vh;
  /* 使用极浅的淡蓝色作为全局背景，映衬天蓝色顶栏 */
  background-color: #f0f7ff;
}

.header {
  /* 天蓝色背景 */
  background-color: #70c5ff;
  display: flex;
  align-items: center;
  padding: 0 50px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: white; /* Logo改为白色 */
  cursor: pointer;
  margin-right: 40px;
}

/* 覆盖 Element Plus 默认菜单样式，使其透明并文字变白 */
.el-menu {
  background-color: transparent !important;
  border-bottom: none !important;
}

.el-menu-item,
.el-sub-menu__title {
  color: rgba(255, 255, 255, 0.9) !important;
  font-weight: 500;
}

.el-menu-item:hover,
.el-menu-item.is-active {
  color: white !important;
  background-color: rgba(255, 255, 255, 0.2) !important;
}

.el-sub-menu__title:hover {
  color: white !important;
  background-color: rgba(255, 255, 255, 0.2) !important;
}

/* 子菜单样式修复 - 使用更高优先级选择器 */
.el-menu--popup {
  background-color: white !important;
  border: none !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-menu--popup .el-menu-item {
  color: #70c5ff !important;
  background-color: white !important;
  transition: all 0.3s ease;
}

.el-menu--popup .el-menu-item:hover {
  color: white !important;
  background-color: #70c5ff !important;
  transform: translateX(5px);
}

.el-menu--popup .el-menu-item.is-active {
  color: white !important;
  background-color: #5fb5ef !important;
}

.flex-grow {
  flex-grow: 1;
}

.main {
  padding: 20px 50px;
  width: 1400px;
  margin: 0 auto;
}
</style>
