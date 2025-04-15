<template>
  <div class="div_class">
    <h3>You will be redirected to payment page</h3>
    <div class="alert alert-primary">
      <!--https://docs.stripe.com/testing-->
      While making payment use card number 4242 4242 4242 4242 and enter random cvv (3 digit)
    </div>
    <button class="btn btn-primary" @click="goToCheckout">Make Payment</button>
  </div>
</template>
<script>
import axios from "axios";
export default {
  name: 'Checkout',
  props: ["baseURL"],
  data() {
    return {
      // public key fromm https://dashboard.stripe.com/test/apikeys
      stripeAPIToken: 'pk_test_51QiwEXGDIGg2zH7xIu66Vj7Aq5LeyrlvWS3L3umIJp4s8iLoufxNHGuwcSO6w4Or5jYNPzaAKVBegNLvw80FR1uj006Lvmpr9q',
      stripe: '',
      token: null,
      checkoutBodyArray: [] // List <CheckoutItemDto>
    };
  },
  methods: {
    getAllItems() {
      axios.get(`${this.baseURL}cart/?token=${this.token}`)
          .then(res => {
            if(res.status == 200) {
              let products = res.data;
              for(let i = 0; i < products.cartItemDtos.length; i++) {
                this.checkoutBodyArray.push({
                  // CheckoutItemDto Object
                  price: products.cartItemDtos[i].product.price,
                  quantity: products.cartItemDtos[i].quantity,
                  productName: products.cartItemDtos[i].product.name,
                  productId: products.cartItemDtos[i].product.id

                })
              }
            }
          })
          .catch(err => console.log("err ", err));
    },
    goToCheckout() {
      axios.post(`${this.baseURL}order/create-checkout-session`, this.checkoutBodyArray)
          .then(res => {
            console.log("Checkout response:", res.data); // Debugging
            localStorage.setItem("sessionId", res.data.sessionId);
            this.stripe.redirectToCheckout({
              sessionId: res.data.sessionId,
            });
          })
    }
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.stripe = window.Stripe(this.stripeAPIToken);
    this.getAllItems();
  }
}
</script>
<style scoped>
.alert {
  width: 50%;
}

.div_class {
  margin-top: 5%;
  margin-left: 30%;
}

</style>