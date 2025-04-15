<template>
<div class="text-center">
  <h1>Your Order</h1>
</div>
  <div class="card p-3 mb-4 bg-light shadow-sm mx-auto"
       style="max-width: 800px"
       v-for="(ordersOnDate, date) in groupedOrders" :key="date">

    <!-- clickable header -->
    <div class="mb-1 d-flex justify-content-between align-items-center"
         style="cursor: pointer"
         @click="toggleDate(date)"><strong>Order Date:</strong> {{date}}
      <span>{{ isExpanded(date) ? '▼' : '►' }}</span>
    </div>

    <!-- expandable section -->
    <transition name="fade-expand">
      <div v-if="isExpanded(date)">
        <div v-for="(order, index) in ordersOnDate"
             :key="index"
             class="border rounded p-3 mt-3 bg-white">
          <p class="fw-bold mb-2">Ordered at: {{ formatTime(order.orderDate) }}</p>
          <p class="mb-0"><strong>Total:</strong> {{ order.totalCost.toFixed(2) }} €</p>
          <div v-for="item in order.items" :key="item.productName">
            <div class="card mb-2 p-2">
              <div class="row g-0">
                <div class="col-md-4">
                  <img :src="item.imageURL" class="img-fluid rounded-start" style="height: 100%; object-fit: cover;" />
                </div>
                <div class="col-md-8">
                  <div class="card-body">
                    <p><strong>Product:</strong> {{ item.productName }}</p>
                    <p><strong>Price:</strong> {{ item.purchasePrice }}</p>
                    <p><strong>Quantity:</strong> {{ item.quantity }}</p>
                    <p><strong>Line Total:</strong> {{ item.lineTotal }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
export default {
  props: ["baseURL"],
  data() {
    return {
      token: null,
      orders: [],
      expandedDates: {}
    }
  },
  computed: {
     groupedOrders() {
      let groups = {};
      for(let order of this.orders) {
        let formattedDate = new Date(order.orderDate).toLocaleDateString();
        if(!groups[formattedDate]) {
          groups[formattedDate] = []; // niitialize it as an empty array
        }
        groups[formattedDate].push(order);
      }
      return groups;
    },
  },
  methods: {
    placeOrder() {
      return this.$axios.post(`${this.baseURL}order/place?token=${this.token}`)
    },
    async fetchOrders() {
      const res = await this.$axios.get(`${this.baseURL}order/user?token=${this.token}`)
      this.orders = res.data;
    },
    toggleDate(date) {
      this.expandedDates[date] = !this.expandedDates[date];
    },
    isExpanded(date) {
      return this.expandedDates[date];
    },
    formatTime(date) {
      return new Date(date).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    }
  },
  async mounted() {
    this.token = localStorage.getItem("token");
    const orderAlreadyPlaced = sessionStorage.getItem("orderPlaced") === "true"; // sessionStorage is cleared when the tab/browser is closed
    if(!orderAlreadyPlaced) {
      await this.placeOrder();
      sessionStorage.setItem("orderPlaced", "true"); //  the order is: Placed only once after payment, Not duplicated on refresh or returning to /payment/success
    }
    await this.fetchOrders();
  }
}
</script>
<style scoped>
.fade-expand-enter-active,
.fade-expand-leave-active {
  transition: all 0.7s ease;
  overflow: hidden;
}

.fade-expand-enter-from,
.fade-expand-leave-to {
  max-height: 0;
  opacity: 0;
}

.fade-expand-enter-to,
.fade-expand-leave-from {
  max-height: 1000px; /* Big enough to fit the content */
  opacity: 1;
}

</style>
