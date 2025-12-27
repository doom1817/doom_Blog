<template>
  <div class="home-container">
    <!-- 顶部横幅 -->
    <div class="welcome-banner">
      <h1 class="welcome-title">欢迎来到我的个人博客</h1>
      <p class="welcome-subtitle">分享技术，记录生活，遇见有趣的灵魂。</p>
    </div>

    <!-- 分类筛选 -->
    <el-card class="category-card" shadow="never">
      <el-tabs v-model="activeCategory" @tab-click="handleTabClick">
        <el-tab-pane label="🌟 全部内容" name="0" />
        <el-tab-pane
          v-for="c in categories"
          :key="c.id"
          :label="'# ' + c.name"
          :name="String(c.id)"
        />
      </el-tabs>
    </el-card>

    <div class="article-list-wrapper">
      <el-empty v-if="articles.length === 0" description="暂无文章" />

      <el-card
        v-for="item in articles"
        :key="item.id"
        class="article-item-card"
        shadow="hover"
      >
        <div class="card-content" @click="goDetail(item.id)">
          <h2 class="article-title">{{ item.title }}</h2>
          <p class="article-summary">{{ item.summary }}</p>
          <div class="article-footer">
            <el-space>
              <el-tag size="small" round>{{
                getCatName(item.categoryId)
              }}</el-tag>
              <span class="footer-item"
                ><el-icon><Calendar /></el-icon>
                {{ item.createTime.split("T")[0] }}</span
              >
              <span class="footer-item"
                ><el-icon><View /></el-icon> {{ item.viewCount }}</span
              >
            </el-space>
            <el-button type="primary" link>阅读全文 →</el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import request from "../utils/request";
import { useRouter } from "vue-router";
import { CollectionTag, Calendar, View, User } from "@element-plus/icons-vue";

const router = useRouter();
const articles = ref([]);
const categories = ref([]);
const activeCategory = ref("0");

const loadCategories = async () => {
  const res = await request.get("/admin/category/list");
  categories.value = res.data;
};

const getCatName = (id) => {
  const cat = categories.value.find((c) => c.id === id);
  return cat ? cat.name : "未分类";
};

const loadArticles = async (catId) => {
  const url =
    catId && catId !== "0"
      ? `/public/article/list?categoryId=${catId}`
      : "/public/article/list";
  const res = await request.get(url);
  articles.value = res.data;
};

const handleTabClick = (tab) => {
  loadArticles(tab.props.name === "0" ? null : tab.props.name);
};

const goDetail = (id) => {
  router.push(`/article/${id}`);
};

onMounted(() => {
  loadCategories();
  loadArticles();
});
</script>

<style scoped>
.home-container {
  width: 1400px;
  margin: 0 auto; /* 居中显示 */
}

/* 1. 欢迎横幅样式 */
.welcome-banner {
  background: linear-gradient(135deg, #70c5ff 0%, #a0d8ff 100%);
  padding: 40px;
  border-radius: 16px;
  color: white;
  margin-bottom: 25px;
  text-align: center;
}
.welcome-title {
  font-size: 32px;
  margin-bottom: 10px;
}
.welcome-subtitle {
  font-size: 16px;
  opacity: 0.9;
}

/* 2. 分类卡片样式 */
.category-card {
  border-radius: 12px;
  margin-bottom: 20px;
  border: none;
}
:deep(.el-tabs__header) {
  margin-bottom: 0;
}
:deep(.el-tabs__nav-wrap::after) {
  height: 0;
}

/* 3. 文章列表卡片样式 */
.article-item-card {
  border-radius: 16px;
  margin-bottom: 25px;
  transition: all 0.3s ease;
  border: 1px solid #eef2f8;
}
.article-item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(112, 197, 255, 0.15) !important;
}
.card-content {
  padding: 25px;
}
.article-title {
  font-size: 22px;
  color: #2c3e50;
  margin: 0 0 15px 0;
  cursor: pointer;
}
.article-title:hover {
  color: #70c5ff;
}
.article-summary {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 20px;
  font-size: 15px;
}

/* 4. 卡片底部样式 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px dashed #eee;
  padding-right: 100px; /* 添加右侧内边距，防止按钮超出 */
}
.footer-left,
.footer-right {
  display: flex;
  align-items: center;
  gap: 15px;
}
.footer-item {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 5. 侧边栏样式 */
.sidebar-card {
  border-radius: 16px;
  border: none;
}
.sidebar-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
}
.author-info {
  text-align: center;
  padding: 10px 0;
}
.author-name {
  margin: 15px 0 5px;
  font-size: 18px;
}
.author-bio {
  color: #909399;
  font-size: 14px;
}
.blog-stats {
  display: flex;
  justify-content: space-around;
}
.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.stat-item b {
  font-size: 20px;
  color: #70c5ff;
}
.stat-item span {
  font-size: 12px;
  color: #909399;
}
</style>
