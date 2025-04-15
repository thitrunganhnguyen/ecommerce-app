<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">{{ category.categoryName }}</h4>
        <h5 style="font-weight: 30; color: #686868">{{ productCountMessage }}</h5>
      </div>
    </div>
      <div class="row">
        <div v-for="product of category.products" :key="product.id"
             class="col-xl-4 col-md-6 col-12 pt-3 justify-contend-around d-flex">
          <ProductBox :product="product"></ProductBox>
        </div>
      </div>
  </div>
</template>
<script>

import ProductBox from "@/components/Product/ProductBox.vue";

export default {
  components: {ProductBox},
  props: ["categories"],
  data() {
    return {
      id: null,
      category: {},
      productCount: 0
    };
  },
  computed: {
    productCountMessage() {
      return this.productCount === 0
          ? "No products found"
          : this.productCount === 1
              ? "Only 1 product found"
              : `${this.productCount} products found`;
    }
  },
  mounted() {
    this.id = this.$route.params.id; // ✅ Get category ID from route
    this.productCount = parseInt(this.$route.query.productCount) || 0; // ✅ Get product count from query
    this.category = this.categories.find(category => category.id == this.id);
  }
};
</script>