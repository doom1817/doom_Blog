<template>
  <div class="admin-stats-container">
    <!-- 1. 顶部数据看板 (动态) -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="总文章数" :value="summary.articleCount">
            <template #prefix
              ><el-icon><Document /></el-icon
            ></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="总用户数" :value="summary.userCount">
            <template #prefix
              ><el-icon><User /></el-icon
            ></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="全站访问量" :value="summary.viewCount">
            <template #prefix
              ><el-icon><View /></el-icon
            ></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="内容分类" :value="summary.categoryCount">
            <template #prefix
              ><el-icon><CollectionTag /></el-icon
            ></template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. 图表区域 (动态) -->
    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 柱状图 -->
      <el-col :span="14">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon><Histogram /></el-icon><span> 阅读量排行 Top 10</span>
            </div>
          </template>
          <div id="barChart" style="height: 400px"></div>
        </el-card>
      </el-col>

      <!-- 饼图 (动态加载后台数据) -->
      <el-col :span="10">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon><PieChart /></el-icon><span> 各分类文章占比</span>
            </div>
          </template>
          <div id="pieChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive } from "vue";
import * as echarts from "echarts";
import request from "../utils/request";
import {
  Document,
  User,
  View,
  CollectionTag,
  Histogram,
  PieChart,
} from "@element-plus/icons-vue";

// 1. 响应式数据绑定顶部卡片
const summary = reactive({
  articleCount: 0,
  userCount: 0,
  viewCount: 0,
  categoryCount: 0,
});

// 加载顶部汇总数据
const loadSummary = async () => {
  const res = await request.get("/admin/article/summary");
  // 关键：将后端返回的对象赋值给响应式对象
  Object.assign(summary, res.data);
};

// 2. 初始化柱状图 (阅读量排行)
const initBarChart = async () => {
  const res = await request.get("/admin/article/stats/clicks");
  const chart = echarts.init(document.getElementById("barChart"));
  chart.setOption({
    tooltip: { trigger: "axis" },
    xAxis: {
      type: "category",
      data: res.data.map((i) => i.title.substring(0, 5) + "..."),
      axisLabel: { color: "#909399", interval: 0, rotate: 20 },
    },
    yAxis: { type: "value" },
    series: [
      {
        data: res.data.map((i) => i.viewCount),
        type: "bar",
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#70c5ff" }, // 天蓝色渐变
            { offset: 1, color: "#a0d8ff" },
          ]),
        },
      },
    ],
  });
};

// 3. 初始化饼图 (分类占比 - 动态接口)
const initPieChart = async () => {
  const res = await request.get("/admin/article/stats/categories");
  const chart = echarts.init(document.getElementById("pieChart"));

  // 天蓝色系配色方案
  const colorPalette = [
    "#70c5ff",
    "#91cc75",
    "#fac858",
    "#ee6666",
    "#73c0de",
    "#3ba272",
    "#fc8452",
    "#9a60b4",
  ];

  chart.setOption({
    tooltip: { trigger: "item", formatter: "{b}: {c} 篇 ({d}%)" },
    legend: { bottom: "0", left: "center" },
    series: [
      {
        name: "文章占比",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: "#fff", borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: "18", fontWeight: "bold" } },
        // 关键：这里直接使用后端返回的 [{name: 'xxx', value: 12}, ...]
        data: res.data.map((item, index) => ({
          ...item,
          itemStyle: { color: colorPalette[index % colorPalette.length] },
        })),
      },
    ],
  });
};

onMounted(() => {
  loadSummary();
  initBarChart();
  initPieChart();
});
</script>

<style scoped>
.admin-stats-container {
  width: 100%;
}
.stat-card {
  border-radius: 12px;
  border: none;
  background: #fff;
}
:deep(.el-statistic__content) {
  color: #70c5ff;
  font-weight: bold;
}
.chart-card {
  border-radius: 16px;
  border: none;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
}
.card-header .el-icon {
  color: #70c5ff;
}
</style>
