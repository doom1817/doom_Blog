import axios from "axios";
import { ElMessage } from "element-plus";

const request = axios.create({
  baseURL: "/api", // 指向你的后端地址
  timeout: 5000,
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.msg || "服务器错误");
      return Promise.reject(new Error(res.msg || "Error"));
    }
    return res;
  },
  (error) => {
    ElMessage.error(error.response?.data?.msg || "网络异常");
    return Promise.reject(error);
  }
);

export default request;
