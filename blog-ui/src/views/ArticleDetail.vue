<template>
  <!-- 只有当 article 数据加载完成且包含 ID 时才渲染主体 -->
  <div class="detail-wrapper" v-if="article && article.id">
    <el-card class="article-container" shadow="never">
      <!-- 1. 顶部返回导航 -->
      <el-page-header
        @back="$router.back()"
        title="返回"
        style="margin-bottom: 30px"
      >
        <template #content>
          <span class="header-title-text"> 阅读文章 </span>
        </template>
      </el-page-header>

      <!-- 2. 文章头部信息 -->
      <div class="article-header">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <el-space :size="20">
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ article.createTime?.split("T")[0] }}
            </span>
            <span class="meta-item">
              <el-icon><View /></el-icon> {{ article.viewCount }} 阅读
            </span>
            <span class="meta-item">
              <el-icon><User /></el-icon> {{ article.nickname || "匿名" }}
            </span>
            <el-tag size="small" class="custom-tag">
              # {{ article.categoryName || "未分类" }}
            </el-tag>
          </el-space>
        </div>

        <!-- 摘要区域 -->
        <div class="summary-box" v-if="article.summary">
          <div class="summary-label">摘要</div>
          <div class="summary-content">
            {{ article.summary }}
          </div>
        </div>
      </div>

      <el-divider />

      <!-- 3. 文章正文内容：使用 v-md-editor 的预览模式 -->
      <div class="article-content">
        <v-md-editor
          :model-value="article.content"
          mode="preview"
          ref="preview"
        ></v-md-editor>
      </div>

      <el-divider />

      <!-- 4. 评论区块 -->
      <div class="comment-section">
        <div class="section-header">
          <el-icon><ChatLineRound /></el-icon>
          <span class="section-title">评论交流 ({{ comments.length }})</span>
        </div>

        <!-- 发表评论 -->
        <div class="comment-input-area">
          <el-input
            v-model="commentText"
            type="textarea"
            :rows="3"
            placeholder="分享你的见解..."
            maxlength="200"
            show-word-limit
          />
          <div class="submit-btn">
            <el-button type="primary" round @click="submitComment"
              >发表评论</el-button
            >
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <el-empty v-if="comments.length === 0" description="暂无评论" />

          <div v-for="c in comments" :key="c.id" class="comment-item">
            <el-avatar
              :size="40"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            />
            <div class="comment-content">
              <div class="comment-info">
                <span class="nickname">{{ c.nickname || "匿名用户" }}</span>
                <div class="info-right">
                  <span class="time">{{ c.createTime }}</span>
                  <el-button
                    v-if="c.userId === currentUserId"
                    type="danger"
                    size="small"
                    link
                    @click="deleteComment(c.id)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
              <div class="text">{{ c.content }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>

  <div v-else class="loading-box">
    <el-skeleton :rows="10" animated />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import request from "../utils/request";
import { ElMessage, ElMessageBox } from "element-plus";
import { Calendar, View, ChatLineRound, User } from "@element-plus/icons-vue";

const route = useRoute();
const article = ref({});
const comments = ref([]);
const commentText = ref("");
const currentUser = ref(localStorage.getItem("username") || "");
const currentUserId = ref(parseInt(localStorage.getItem("userId")) || 0);

const loadData = async () => {
  try {
    const artRes = await request.get(`/public/article/${route.params.id}`);
    article.value = artRes.data;
    const comRes = await request.get(`/public/comments/${route.params.id}`);
    comments.value = comRes.data;
  } catch (error) {
    console.error("加载详情失败", error);
  }
};

const submitComment = async () => {
  // 检查是否登录
  const token = localStorage.getItem("token");
  if (!token) {
    ElMessage.warning("请先登录后再发表评论");
    return;
  }

  if (!commentText.value.trim()) return ElMessage.warning("内容不能为空");
  await request.post("/user/comment/add", {
    articleId: route.params.id,
    content: commentText.value,
  });
  ElMessage.success("评论成功");
  commentText.value = "";
  loadData();
};

const deleteComment = async (id) => {
  try {
    await ElMessageBox.confirm("确定要删除这条评论吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    await request.delete(`/user/comment/delete/${id}`);
    ElMessage.success("删除成功");
    loadData();
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除评论失败", error);
    }
  }
};

onMounted(loadData);
</script>

<style scoped>
.detail-wrapper {
  padding: 30px 0;
}
.article-container {
  border-radius: 16px;
  border: none;
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 60px;
}
.header-title-text {
  font-weight: bold;
  color: #70c5ff;
}

.article-header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 32px;
  color: #2c3e50;
}
.meta {
  color: #909399;
  font-size: 14px;
  margin-bottom: 30px;
}
.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 统一的天蓝色标签 */
.custom-tag {
  background-color: #e1f2ff !important;
  color: #70c5ff !important;
  border-color: #b3dfff !important;
}

/* 摘要盒 */
.summary-box {
  background-color: #f0f9ff;
  padding: 25px 30px;
  border-radius: 12px;
  border-left: 6px solid #70c5ff;
  text-align: left;
  margin: 30px auto 0;
  max-width: 100%;
}
.summary-label {
  font-weight: bold;
  color: #70c5ff;
  font-size: 12px;
  text-transform: uppercase;
  margin-bottom: 8px;
}
.summary-content {
  color: #5a6a7a;
  line-height: 1.6;
  font-size: 15px;
}

/* 正文样式排版 - 穿透修改 Markdown 预览器样式 */
.article-content {
  margin: 30px 0;
  max-width: 100%;
}

/* 移除预览器的边框、阴影和背景 */
:deep(.v-md-editor) {
  background-color: transparent !important;
  box-shadow: none !important;
  border: none !important;
}

:deep(.github-markdown-body) {
  padding: 0 !important;
  background-color: transparent !important;
  color: #374151 !important;
  line-height: 2 !important;
  font-size: 18px !important;
}

/* 评论区样式保持一致 */
.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 25px;
}
.section-title {
  font-size: 20px;
  font-weight: bold;
  color: #2c3e50;
}
.comment-input-area {
  margin-bottom: 40px;
}
.submit-btn {
  text-align: right;
  margin-top: 15px;
}
/* 覆盖按钮颜色 */
:deep(.el-button--primary) {
  background-color: #70c5ff !important;
  border-color: #70c5ff !important;
}

.comment-item {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
}
.comment-content {
  flex: 1;
  border-bottom: 1px solid #f3f4f6;
  padding-bottom: 15px;
}
.comment-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.nickname {
  font-weight: bold;
  color: #70c5ff;
}
.info-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.time {
  font-size: 12px;
  color: #9ca3af;
}
.text {
  color: #4b5563;
  font-size: 14px;
  line-height: 1.5;
}

.loading-box {
  max-width: 1400px;
  margin: 50px auto;
  padding: 40px;
  background: white;
  border-radius: 16px;
}
</style>
