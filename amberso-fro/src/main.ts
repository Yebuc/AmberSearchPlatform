import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import Antd from "ant-design-vue";
// import "ant-design-vue/dist/antd.css";
import "ant-design-vue/dist/reset.css";
//注意版本升级了，不再是antd.css了

// const app = createApp();
// app.config.productionTip = false;

// app.use(Antd);

createApp(App).use(Antd).use(router).mount("#app");
