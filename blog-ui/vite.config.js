import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 代理所有以 /api 开头的请求
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        // 后端接口本身带 /api，所以不需要 rewrite
      },
    },
  },
});
