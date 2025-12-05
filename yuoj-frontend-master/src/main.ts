import { createApp } from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import "@arco-design/web-vue/dist/arco.css";
import router from "./router";
import store from "./store";
import "@/plugins/axios";
import "@/access";
import "bytemd/dist/index.css";

// main.js 或入口文件
const ignoreErrors = [
  "ResizeObserver loop completed with undelivered notifications",
  "ResizeObserver loop limit exceeded",
];

window.addEventListener("error", (e) => {
  const errorMsg = e.message;
  ignoreErrors.forEach((m) => {
    if (errorMsg.startsWith(m)) {
      console.error(errorMsg);
      if (e.error) {
        console.error(e.error.stack);
      }

      // 隐藏开发环境 overlay 报错界面
      const resizeObserverErrDiv = document.getElementById(
        "webpack-dev-server-client-overlay-div"
      );
      const resizeObserverErr = document.getElementById(
        "webpack-dev-server-client-overlay"
      );
      if (resizeObserverErr) {
        resizeObserverErr.setAttribute("style", "display: none");
      }
      if (resizeObserverErrDiv) {
        resizeObserverErrDiv.setAttribute("style", "display: none");
      }
    }
  });
});
window.addEventListener("unhandledrejection", (event) => {
  if (
    event.reason?.message?.includes("ResizeObserver") ||
    event.reason?.message?.includes("ResizeObserver loop")
  ) {
    event.preventDefault();
    event.stopImmediatePropagation();
    return false;
  }
});

// 创建应用实例
const app = createApp(App);

// 使用插件
app.use(ArcoVue);
app.use(ArcoVueIcon);
app.use(store);
app.use(router);

// 挂载应用
app.mount("#app");
