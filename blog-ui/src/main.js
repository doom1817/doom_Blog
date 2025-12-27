import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

// 引入 Markdown 编辑器
import VMdEditor from "@kangc/v-md-editor";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import githubTheme from "@kangc/v-md-editor/lib/theme/github.js";
import "@kangc/v-md-editor/lib/theme/style/github.css";

// 引入代码高亮
import hljs from "highlight.js";
import "highlight.js/styles/github.css";

VMdEditor.use(githubTheme, {
  Hljs: hljs,
});

const app = createApp(App);
app.use(router);
app.use(ElementPlus);
app.use(VMdEditor); // 这里是 Vue 3 的注册方式
app.mount("#app");
