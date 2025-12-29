<!-- src/views/AdminAudit.vue -->
<template>
  <div class="admin-audit-container">
    <el-card>
      <template #header>
        <div class="header">
          <el-icon><Checked /></el-icon> <span>待审核文章管理</span>
        </div>
      </template>

      <el-table :data="list" style="width: 100%" v-loading="loading">
        <el-table-column
          prop="title"
          label="文章标题"
          min-width="200"
          show-overflow-tooltip
        />

        <!-- 新增：作者信息列 -->
        <el-table-column label="作者信息" width="180">
          <template #default="scope">
            <div style="display: flex; align-items: center; gap: 8px">
              <el-avatar :size="24" icon="UserFilled" />
              <span>{{ scope.row.nickname }}</span>
              <span style="color: #999; font-size: 12px"
                >(@{{ scope.row.username }})</span
              >
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="categoryName" label="所属分类" width="120">
          <template #default="scope">
            <el-tag size="small" effect="plain">{{
              scope.row.categoryName || "未分类"
            }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="投稿时间" width="180" />

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              type="success"
              size="small"
              plain
              @click="audit(scope.row.id, 1)"
              >通过</el-button
            >
            <el-button
              type="danger"
              size="small"
              plain
              @click="showRejectDialog(scope.row.id)"
              >驳回</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 驳回理由输入对话框 -->
      <el-dialog v-model="rejectDialogVisible" title="驳回文章" width="400px">
        <el-form label-position="top">
          <el-form-item label="驳回理由">
            <el-input
              v-model="rejectReason"
              type="textarea"
              :rows="4"
              placeholder="请输入驳回理由"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject">确定驳回</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import request from "../utils/request";
import { ElMessage } from "element-plus";
import { Checked } from "@element-plus/icons-vue";

const list = ref([]);
const loading = ref(false);
const rejectDialogVisible = ref(false);
const rejectReason = ref("");
const currentAuditId = ref(null);

const load = async () => {
  loading.value = true;
  // 调用管理员审核列表接口
  const res = await request.get("/admin/article/audit/list");
  list.value = res.data;
  loading.value = false;
};

const audit = async (id, status) => {
  await request.post("/admin/article/audit", { id, status });
  ElMessage.success(status === 1 ? "审核已通过" : "已驳回该文章");
  load();
};

// 显示驳回对话框
const showRejectDialog = (id) => {
  currentAuditId.value = id;
  rejectReason.value = "";
  rejectDialogVisible.value = true;
};

// 确认驳回
const confirmReject = async () => {
  if (!rejectReason.value.trim()) {
    ElMessage.warning("请输入驳回理由");
    return;
  }
  await request.post("/admin/article/audit", {
    id: currentAuditId.value,
    status: 2,
    rejectReason: rejectReason.value.trim(),
  });
  ElMessage.success("已驳回该文章");
  rejectDialogVisible.value = false;
  load();
};

onMounted(load);
</script>

<style scoped>
.admin-audit-container {
  width: 1400px;
  margin: 0 auto;
}

.header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  color: #409eff;
}
</style>
