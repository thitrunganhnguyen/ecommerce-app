<template>
  <Navbar :cartCount="cartCount" @resetCartCount="resetCartCount" @search="onSearch"/>
  <!--
  <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>
  </nav>
  -->
  <!-- the data is only passes to other component if they are not null anymore -->
  <!--set min-height to make footer locate at the end of page -->
  <router-view v-if="categories && products"
               style="min-height: 60vh"
  :baseURL="baseURL"
  :categories="categories"
  :products="products"
  :searchQuery="searchQuery"
  @fetchData="fetchData">
  </router-view>

  <Footer />

</template>
<script>
import axios from "axios";
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";

export default {
  components: {Navbar, Footer},
  data() {
    return {
      baseURL: "http://localhost:8092/", // backend
      // baseURL : "https://limitless-lake-55070.herokuapp.com/",
      categories: null,
      products: null,
      cartCount: 0,
      searchQuery: '',
    }
  },
  methods: {
    async fetchData() {
      // api call to get all the categories
      await axios.get(this.baseURL + "category/")
          .then(res => this.categories = res.data)
          .catch(err => console.log('err', err));

      // api call to get the products
      await axios.get(this.baseURL + "product/")
          .then(res => this.products = res.data)
          .catch(err => console.log('err', err));

      // fetch cart item if token is present i.e logged in
      if(this.token) {
        axios.get(`${this.baseURL}cart/?token=${this.token}`)
            .then(res => {
              const result = res.data;
              this.cartCount = result.cartItemDtos.length;
            }).catch(err => console.log("err", err));
      }
    },
    // set cart count = 0 when logged out
    resetCartCount() {
      this.cartCount = 0;
    }
    ,
    onSearch(query) {
      this.searchQuery = query.toLowerCase();
      console.log("searchQuery ", this.searchQuery);
    }
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.fetchData();
  }
}
</script>
<style>
html{
  overflow-y: scroll;
}
</style>
