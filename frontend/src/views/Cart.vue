<template>
  <div class="container py-4">
    <div class="text-center mb-4">
      <h3>🛒 Shopping Cart</h3>
    </div>

    <div v-if="cartItems.length === 0" class="text-center text-muted">
      Your cart is empty.
    </div>

    <div v-else>
      <div v-for="cartItem in cartItems"
           :key="cartItem.id"
           class="row justify-content-center mb-4">
        <div class="card shadow-sm p-0" style="max-width: 800px;">
          <div class="row g-0">
            <div class="col-md-4">
              <img :src="cartItem.product.imageURL"
                   alt="Product Image"
                   class="img-fluid rounded-start"
                   style="height: 100%; object-fit: cover;" />
            </div>
            <div class="col-md-8">
              <div class="card-body">
                <h5 class="card-title">
                  <router-link :to="{name: 'ShowDetails', params: {id: cartItem.product.id}}">
                    {{ cartItem.product.name }}
                  </router-link>
                </h5>
                <p class="card-text mb-1">
                  <strong>{{ cartItem.product.price }} €</strong> per unit
                </p>
                <div class="d-flex align-items-center mb-2">
                  <label class="me-2 mb-0">Quantity: </label>
                  <input type="number"
                         min="1"
                         class="form-control form-control-sm"
                         style="width: 80px"
                         v-model.number="cartItem.quantity"
                         @change="updateQuantity(cartItem)">
                </div>
                <p class="card-text mb-2">
                  Total:
                  <strong>{{ (cartItem.product.price * cartItem.quantity).toFixed(2) }} €</strong>
                </p>
                <button class="btn btn-sm btn-outline-danger" @click="deleteItem(cartItem.id)">
                  Remove
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Total and Checkout -->
      <div class="row mt-4 align-items-center justify-content-center">
        <div class="col-12 col-md-8">
          <div class="d-flex justify-content-between flex-wrap">
            <h5 class="mb-2 mb-md-0">Total: <strong>{{ totalCost.toFixed(2) }} €</strong></h5>
            <button type="button" class="btn btn-primary" @click="checkout">
              Confirm Order
            </button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: ["baseURL"],
  data() {
    return {
      cartItems: [],
      token: null,
      totalCost: 0
    }
  },
  methods: {
    listCartItems(){
      axios.get(`${this.baseURL}cart/?token=${this.token}`)
          .then(res => {
            const result = res.data;
            this.cartItems = result.cartItemDtos;
            this.totalCost = result.totalCost;
          }).catch(err => console.log("err", err));
    },
    deleteItem(itemId) {
      axios.delete(`${this.baseURL}cart/delete/${itemId}?token=${this.token}`)
          .then(res => {
            if(res.status == 200) {
              // this.$router.go(0); // reload the current page, similar to window.location.reload()
              this.listCartItems();
            }
          })
          .catch(err => console.log("err", err));
    },
    updateQuantity(cartItem) {
      const updatedItem = {
        productId: cartItem.product.id,
        quantity: cartItem.quantity,
      };
      axios.put(`${this.baseURL}cart/update/${cartItem.id}?token=${this.token}`, updatedItem)
          .then(() => {
            this.listCartItems(); // Refresh the cart
          })
          .catch(err => {
            console.log("Update error:", err);
            // Optionally show a toast or error message
          });
    },

    checkout() {
      sessionStorage.removeItem("orderPlaced");
      this.$router.push({name: 'Checkout'});
    }
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.listCartItems();
  }
}
</script>
<style scoped>
.card-title a {
  text-decoration: none;
  color: #333;
}
.card-title a:hover {
  text-decoration: underline;
}

.card {
  border-radius: 12px;
}

</style>