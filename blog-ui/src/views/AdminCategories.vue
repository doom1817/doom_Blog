<template>
  <div class="admin-categories-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchQuery"
              placeholder="搜索分类名称..."
              prefix-icon="Search"
              style="width: 250px; margin-right: 15px"
              clearable
            />
            <el-button type="primary" @click="addVisible = true" class="add-btn"
              >新增分类</el-button
            >
          </div>
        </div>
      </template>
      <el-table :data="filteredCategories">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="名称" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="danger" size="small" @click="del(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="addVisible" title="新增分类">
        <el-input v-model="newName" placeholder="请输入分类名称" />
        <template #footer>
          <el-button @click="addVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import request from "../utils/request";
import { ElMessage } from "element-plus";

const categories = ref([]);
const searchQuery = ref("");
const addVisible = ref(false);
const newName = ref("");

// 搜索过滤
const filteredCategories = computed(() => {
  return categories.value.filter((category) =>
    category.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const load = async () => {
  const res = await request.get("/admin/category/list");
  categories.value = res.data;
};
const save = async () => {
  await request.post("/admin/category/add", { name: newName.value });
  ElMessage.success("添加成功");
  addVisible.value = false;
  newName.value = "";
  load();
};
const del = async (id) => {
  try {
    await request.delete(`/admin/category/delete/${id}`);
    ElMessage.success("删除成功");
    load();
  } catch (error) {
    // 捕获后端返回的错误信息
    ElMessage.error(error.response?.data?.message || "删除失败");
  }
};
onMounted(load);
</script>

<style scoped>
.admin-categories-container {
  width: 1400px;
  margin: 0 auto;
}

:deep(.el-card) {
  width: 100%;
  padding: 20px 50px;
  box-sizing: border-box;
}

:deep(.el-card__header) {
  padding: 10px 0;
  box-sizing: border-box;
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.card-header span {
  flex-shrink: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header .el-button {
  flex-shrink: 0;
  white-space: nowrap;
}

.add-btn {
  background-color: #70c5ff !important;
  border-color: #70c5ff !important;
}
</style>
