import Vue from 'vue'
import App from './components/Data.vue'
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';


Vue.config.productionTip = false;

new Vue({
  render: h => h(App),
}).$mount('#app');
