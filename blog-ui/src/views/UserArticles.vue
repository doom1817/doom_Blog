<template>
  <div class="user-articles-container">
    <!-- 1. 顶部操作栏 -->
    <el-card class="box-card" shadow="never">
      <div class="header-wrapper">
        <div class="title-section">
          <el-icon class="title-icon"><Document /></el-icon>
          <!-- 动态标题 -->
          <span class="title-text">{{
            isAdmin ? "全站文章管理" : "我的文章管理"
          }}</span>
        </div>
        <div class="action-section">
          <el-input
            v-model="searchQuery"
            placeholder="搜索文章标题..."
            prefix-icon="Search"
            style="width: 250px; margin-right: 15px"
            clearable
          />
          <el-button type="primary" icon="Plus" @click="handleAdd"
            >发布新文章</el-button
          >
        </div>
      </div>
    </el-card>

    <!-- 2. 数据表格 -->
    <el-card class="table-card" shadow="never" style="margin-top: 20px">
      <el-table
        :data="filteredArticles"
        v-loading="loading"
        style="width: 100%"
        :header-cell-style="{
          backgroundColor: '#f5faff',
          color: '#606266',
          fontWeight: 'bold',
        }"
      >
        <el-table-column prop="id" label="ID" width="70" />

        <el-table-column
          prop="title"
          label="文章标题"
          width="150"
          show-overflow-tooltip
        />

        <!-- 作者列 -->
        <el-table-column label="作者" width="160">
          <template #default="scope">
            <div class="author-cell">
              <span class="nickname">{{ scope.row.nickname }}</span>
              <span class="username">@{{ scope.row.username }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="分类" width="100">
          <template #default="scope">
            <el-tag size="small" type="info" effect="plain" class="custom-tag">
              {{ scope.row.categoryName || "未分类" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="viewCount"
          label="阅读量"
          width="90"
          align="center"
        >
          <template #default="scope">
            <span class="view-count-text"
              ><el-icon><View /></el-icon> {{ scope.row.viewCount }}</span
            >
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 驳回理由列 -->
        <el-table-column label="驳回理由" width="150">
          <template #default="scope">
            <div v-if="scope.row.status === 2" class="reject-reason">
              <el-popover
                placement="top"
                :width="300"
                trigger="hover"
                :content="scope.row.rejectReason"
              >
                <template #reference>
                  <span class="reject-text" :title="scope.row.rejectReason">
                    <el-icon class="icon-warning"><WarningFilled /></el-icon>
                    {{ scope.row.rejectReason || "无理由" }}
                  </span>
                </template>
              </el-popover>
            </div>
            <span v-else class="no-reason">-</span>
          </template>
        </el-table-column>

        <el-table-column
          prop="createTime"
          label="发布时间"
          width="200"
          sortable
          align="center"
        >
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="240">
          <template #default="scope">
            <el-button-group>
              <!-- 仅文章作者本人可以编辑 -->
              <el-button
                v-if="parseInt(scope.row.userId) === parseInt(currentUserId)"
                size="small"
                icon="Edit"
                @click="handleEdit(scope.row)"
                >编辑</el-button
              >

              <!-- 管理员或作者本人可以删除 -->
              <el-button
                size="small"
                type="danger"
                icon="Delete"
                @click="handleDelete(scope.row)"
                >删除</el-button
              >
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 3. 发布/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '🖊️ 编辑文章' : '🚀 发布新文章'"
      width="60%"
      destroy-on-close
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="articleFormRef"
        label-position="top"
      >
        <el-form-item label="文章标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入引人入胜的标题..."
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-select
                v-model="form.categoryId"
                placeholder="选择分类"
                style="width: 100%"
              >
                <el-option
                  v-for="c in categories"
                  :key="c.id"
                  :label="c.name"
                  :value="c.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="文章摘要" prop="summary">
          <el-input
            v-model="form.summary"
            type="textarea"
            placeholder="简要概括文章内容..."
            :rows="3"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="正文内容" prop="content">
          <v-md-editor
            v-model="form.content"
            height="500px"
            placeholder="支持 Markdown 语法..."
            left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol hr | link image code"
          ></v-md-editor>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" round>取消</el-button>
          <el-button type="primary" @click="save" :loading="btnLoading" round
            >提交并进入审核</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import request from "../utils/request";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Document,
  Search,
  Plus,
  View,
  Edit,
  Delete,
  WarningFilled,
} from "@element-plus/icons-vue";

// 权限信息
const isAdmin = ref(localStorage.getItem("role") === "ADMIN");
const currentUserId = ref(localStorage.getItem("userId"));

// 数据定义
const myArticles = ref([]);
const categories = ref([]);
const loading = ref(false);
const btnLoading = ref(false);
const dialogVisible = ref(false);
const searchQuery = ref("");
const articleFormRef = ref(null);

const form = ref({
  id: null,
  title: "",
  content: "",
  categoryId: "",
  summary: "",
});

const rules = {
  title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
  categoryId: [{ required: true, message: "请选择分类", trigger: "change" }],
  content: [{ required: true, message: "正文不能为空", trigger: "blur" }],
};

// 搜索过滤
const filteredArticles = computed(() => {
  return myArticles.value.filter((article) =>
    article.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 加载文章：后端接口已优化为根据角色自动返回“全站”或“个人”数据
const loadArticles = async () => {
  loading.value = true;
  try {
    const res = await request.get("/user/article/list");
    myArticles.value = res.data;
  } finally {
    loading.value = false;
  }
};

const loadCategories = async () => {
  const res = await request.get("/admin/category/list");
  categories.value = res.data;
};

const getStatusType = (status) => {
  const map = { 0: "warning", 1: "success", 2: "danger" };
  return map[status] || "info";
};
const getStatusText = (status) => {
  const map = { 0: "待审核", 1: "已发布", 2: "已驳回" };
  return map[status] || "未知";
};

// 格式化时间，去掉中间的T
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return "";
  return dateTimeStr.replace("T", " ");
};

const handleAdd = () => {
  form.value = {
    id: null,
    title: "",
    content: "",
    categoryId: "",
    summary: "",
  };
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  form.value = { ...row };
  dialogVisible.value = true;
};

const save = async () => {
  articleFormRef.value.validate(async (valid) => {
    if (!valid) return;
    btnLoading.value = true;
    try {
      if (form.value.id) {
        await request.put("/user/article/update", form.value);
      } else {
        await request.post("/user/article/add", form.value);
      }
      ElMessage.success("保存成功");
      dialogVisible.value = false;
      loadArticles();
    } finally {
      btnLoading.value = false;
    }
  });
};

const handleDelete = (row) => {
  const isOwner = parseInt(row.userId) === parseInt(currentUserId.value);
  const message = isOwner
    ? `您确定要删除该《${row.title}》文章吗？`
    : `您正在以管理员身份强制删除用户 [${row.nickname}] 的文章《${row.title}》，确定吗？`;

  ElMessageBox.confirm(message, "警告", {
    confirmButtonText: "确定删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    await request.delete(`/user/article/delete/${row.id}`);
    ElMessage.success("删除成功");
    loadArticles();
  });
};

onMounted(() => {
  loadArticles();
  loadCategories();
});
</script>

<style scoped>
.user-articles-container {
  padding: 10px;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
}
.box-card,
.table-card {
  border-radius: 12px;
  border: none;
}
.header-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 100px; /* 添加右侧内边距，防止按钮超出 */
}
.title-section {
  display: flex;
  align-items: center;
  gap: 10px;
}
.title-icon {
  font-size: 24px;
  color: #70c5ff;
}
.title-text {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
}

/* 管理员作者单元格 */
.author-cell {
  display: flex;
  flex-direction: column;
  padding: 8px 0;
}
.nickname {
  font-weight: 600;
  color: #70c5ff;
  font-size: 14px;
  margin-bottom: 2px;
}
.username {
  font-size: 12px;
  color: #999;
}

/* 天蓝色主题定制 */
.custom-tag {
  background-color: #f0f9ff !important;
  color: #70c5ff !important;
  border-color: #d1ecff !important;
  font-weight: 500;
  border-radius: 6px;
  padding: 4px 12px;
}
:deep(.el-button--primary) {
  background-color: #70c5ff !important;
  border-color: #70c5ff !important;
}
:deep(.el-button--primary:hover) {
  background-color: #8cd1ff !important;
  border-color: #8cd1ff !important;
}
.view-count-text {
  color: #606266;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-weight: 500;
}

/* 驳回理由样式 */
.reject-reason {
  display: flex;
  align-items: center;
}

.reject-text {
  color: #f56c6c;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 4px 8px;
  background-color: #fef0f0;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.reject-text:hover {
  background-color: #fde2e2;
  transform: translateY(-1px);
}

.icon-warning {
  color: #f7ba2a;
  font-size: 14px;
}

.no-reason {
  color: #c0c4cc;
  font-size: 13px;
}

/* 表格美化样式 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

:deep(.el-table__header-wrapper) {
  border-radius: 8px 8px 0 0;
}

:deep(.el-table th.el-table__cell) {
  background: linear-gradient(135deg, #f0f9ff 0%, #e8f4ff 100%) !important;
  color: #2c3e50 !important;
  font-weight: 600;
  font-size: 14px;
  padding: 16px 0;
  border-bottom: 2px solid #d1ecff;
}

:deep(.el-table td.el-table__cell) {
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #f5faff !important;
  transform: scale(1.002);
  box-shadow: 0 2px 8px rgba(112, 197, 255, 0.15);
}

:deep(.el-table__body tr) {
  transition: all 0.3s ease;
}

/* 斑马纹效果 */
:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
  background-color: #f5faff !important;
}

:deep(.el-table__row--striped td) {
  background-color: #fafbfc !important;
}

:deep(.el-table__row--striped:hover td) {
  background-color: #f5faff !important;
}

/* 表格内容样式 */
:deep(.el-table .cell) {
  padding: 0 12px;
  line-height: 1.6;
}

/* ID列样式 */
:deep(.el-table .el-table__cell:first-child) {
  text-align: center;
  color: #909399;
  font-weight: 500;
}

/* 标题列样式 */
:deep(.el-table .el-table__cell:nth-child(2)) {
  color: #2c3e50;
  font-weight: 500;
}

/* 状态标签美化 */
:deep(.el-tag) {
  border-radius: 6px;
  padding: 4px 12px;
  font-weight: 500;
  font-size: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

:deep(.el-tag--success) {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border-color: #67c23a;
}

:deep(.el-tag--warning) {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
  border-color: #e6a23c;
}

:deep(.el-tag--danger) {
  background: linear-gradient(135deg, #f56c6c 0%, #f89898 100%);
  border-color: #f56c6c;
}

:deep(.el-tag:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12);
}

/* 操作按钮组美化 */
:deep(.el-button-group) {
  display: flex;
  gap: 8px;
}

:deep(.el-button--small) {
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
}

:deep(.el-button--small:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 删除按钮样式 - 使用更具体的选择器 */
:deep(.el-button--small.el-button--danger) {
  background: linear-gradient(135deg, #f56c6c 0%, #f89898 100%);
  border-color: #f56c6c;
  color: #fff;
}

:deep(.el-button--small.el-button--danger:hover) {
  background: linear-gradient(135deg, #f78989 0%, #fab6b6 100%);
  border-color: #f78989;
}

/* 编辑按钮样式 - 使用更具体的选择器 */
:deep(.el-button--small:not(.el-button--danger)) {
  background: linear-gradient(135deg, #70c5ff 0%, #8cd1ff 100%);
  border-color: #70c5ff;
  color: #fff;
}

:deep(.el-button--small:not(.el-button--danger):hover) {
  background: linear-gradient(135deg, #8cd1ff 0%, #a8dfff 100%);
  border-color: #8cd1ff;
}

/* 图标样式 */
:deep(.el-icon) {
  vertical-align: middle;
}

/* 加载动画 */
:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
}

:deep(.el-loading-spinner .circular) {
  width: 42px;
  height: 42px;
}

/* 对话框美化 */
:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}
:deep(.el-dialog__header) {
  padding: 24px;
  background: linear-gradient(135deg, #f0f9ff 0%, #e8f4ff 100%);
  margin-right: 0;
  border-bottom: 1px solid #d1ecff;
}
:deep(.el-dialog__title) {
  color: #70c5ff;
  font-weight: 600;
  font-size: 18px;
}
:deep(.el-dialog__body) {
  padding: 24px;
}
:deep(.el-dialog__footer) {
  padding: 16px 24px;
  background-color: #fafbfc;
  border-top: 1px solid #f0f0f0;
}

/* 搜索框美化 */
:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(112, 197, 255, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(112, 197, 255, 0.3);
}

/* 卡片阴影优化 */
:deep(.el-card__body) {
  padding: 20px;
}

/* 时间列样式 */
:deep(.el-table .el-table__cell:nth-last-child(3)) {
  color: #606266;
  font-size: 13px;
}

/* 响应式优化 */
@media (max-width: 1440px) {
  .user-articles-container {
    width: 100%;
    padding: 10px 20px;
  }
}
</style>
