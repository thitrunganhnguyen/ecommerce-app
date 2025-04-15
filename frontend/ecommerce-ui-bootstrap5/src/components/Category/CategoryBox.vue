<template>
  <div class="card w-100 h-100 shadow-sm border-0 rounded-3 overflow-hidden" style="margin-top: 10px">
    <div class="ratio ratio-16x9">
      <img class="card-img-top" :src="category.imageUrl" alt="Category Image">
    </div>

    <div class="card-body">
      <!-- ✅ Pass productCount when navigating -->
      <router-link
          :to="{ name: 'ListProducts', params: { id: category.id }, query: { productCount: productCount } }"
          class="text-decoration-none">
        <h5 class="card-title text-dark fw-bold">{{ category.categoryName }}</h5>
      </router-link>


      <p class="card-text text-muted">{{ category.description }}</p>
      <h6 class="text-muted">{{ productCountMessage }}</h6>

      <div class="d-flex align-items-center" v-if="$route.name == 'Category'">
        <router-link :to="{ name: 'EditCategory', params: { id: category.id }}">
          <button class="btn btn-primary btn-sm me-2">Edit</button>
        </router-link>
        <button class="btn btn-danger btn-sm" @click="requestDelete">Delete</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CategoryBox",
  props: ["category", "productCount"], // ✅ Receive productCount from parent
  computed: {
    productCountMessage() {
      return this.productCount === 0
          ? "No products available"
          : this.productCount === 1
              ? "Only 1 product available"
              : `${this.productCount} products available`;
    }
  },
  methods: {
    requestDelete() {
      console.log(`🛑 Requesting delete for category ID: ${this.category.id}`);
      this.$emit("deleteCategory", this.category);
    }
  }
};
</script>
