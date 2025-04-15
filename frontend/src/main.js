import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import sweetAlert from "sweetalert";
import "bootstrap/dist/css/bootstrap.min.css"; // Ensure Bootstrap styles are included
import "bootstrap/dist/js/bootstrap.bundle.min.js"; // Import Bootstrap JS for components
import 'bootstrap-icons/font/bootstrap-icons.css';

const app = createApp(App);

// Register Axios as a global property
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$sweetAlert = sweetAlert;

app.use(router).mount('#app')
