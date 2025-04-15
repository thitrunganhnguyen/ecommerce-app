<template>
  <div id="home">
    <!-- ✅ Show this only when NOT searching -->
    <div v-if="!isSearching">
      <div id="background-div" class="page-holder bg-cover">
        <div class="container py-5">
          <header class="text-left text-white py-5">
            <h3 class="mb-4 rounded" id="heading">
              <a href="#start-shopping" class="bg-white px-2 py-2 rounded">Start shopping </a>
            </h3>
            <p class="lead mb-0 bg-secondary p-1 rounded demo-text">You only live once, but if you do it right, once is enough.</p>
          </header>
        </div>
      </div>
      <!-- display categories -->
      <div class="container">
        <div class="row">
          <div class="col-12 text-left">
            <h2 class="pt-3">Top Categories</h2>
          </div>
        </div>
        <div class="row">
          <div v-for="index in this.categorySize" :key="index" class="col-md-6 col-xl-4 col-12 pt-3  justify-content-around d-flex">
            <CategoryBox :category="categories[index-1]">
            </CategoryBox>
          </div>
        </div>
      </div>
    </div>
    <!-- ✅ Always show product section -->
    <div class="container py-2">
      <div class="row">
        <div class="col-12 text-left">
          <h2 class="pt-3">Top Products</h2>
        </div>
      </div>
      <div class="row">
        <template v-if="filteredProducts.length > 0">
          <div v-for="product in displayedProducts"
               :key="product.id"
               class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex">
            <ProductBox :product="product" />
          </div>
        </template>
        <template v-else>
          <div class="text-center text-muted py-4">
            <p>No products found for "{{ searchQuery }}"</p>
          </div>
        </template>
      </div>

    </div>
  </div>
</template>

<script>
// @ is an alias to /src

import CategoryBox from "@/components/Category/CategoryBox.vue";
import ProductBox from "@/components/Product/ProductBox.vue";

export default {
  name: 'HomeView',
  components: {ProductBox, CategoryBox},
  props: ["categories", "products", "searchQuery"],
  data() {
    return {
      categorySize: 0,
      productSize: 0,
    }
  },
  computed: {
    filteredProducts() {
      if(!this.searchQuery) return this.products;
      return this.products.filter(product => product.name.toLowerCase().includes(this.searchQuery));
    },
    displayedProducts(){
      // If searching, show all filtered products
      if(this.isSearching) return this.filteredProducts;
      // Otherwise, limit the list
      return this.filteredProducts.slice(0, this.productSize);
    },
    isSearching() {
      return this.searchQuery && this.searchQuery.length > 0;
    }
  },
  mounted() {
    this.categorySize = Math.min(6, this.categories.length);
    this.productSize = Math.min(8, this.products.length);
  }
}
</script>
<style>
.page-holder {
  min-height: 60vh;
  display: flex;
  align-items: center; /* Center content vertically */
  justify-content: center; /* Center content horizontally */
}

#background-div {
  background: url("../assets/360_F_390123053_dLdh2YggDNrXYlGP9OgBGfTdIwUKU2Hx.jpg") no-repeat center;
  background-size: cover;
}
#heading {
  font-weight: 400;
}
.demo-text {
  display: inline-block; /* Keeps the width only as wide as needed */
  text-align: center; /* Ensures text is centered */
  padding: 5px 10px !important; /* Reduce padding */
  max-width: 500px; /* Adjust as needed */
}
</style>
