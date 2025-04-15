<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Your WishList</h4>
      </div>
    </div>
    <div class="row">
      <div v-for="product of products" :key="product.id"
           class="col-xl-4 col-md-6 col-12 pt-3 justify-content-around d-flex">
        <ProductBox :product="product"></ProductBox>
      </div>
    </div>
  </div>

</template>
<script>
import axios from "axios";
import ProductBox from "@/components/Product/ProductBox.vue";
export default {
  components: {ProductBox},
  props: ["baseURL"],
  data(){
    return {
      token: null,
      products: null,
    }
  },
  methods: {
    fetchWishList() {
      axios.get(`${this.baseURL}wishlist/${this.token}`)
          .then(res => {
            this.products = res.data;
          })
          .catch(err => {
            console.log("err", err);
          })
    }
  },
  mounted(){
    this.token = localStorage.getItem("token");
    this.fetchWishList();
  }
};
</script>