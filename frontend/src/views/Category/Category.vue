<template>
  <div class="container mt-4">
    <!-- Header Section -->
    <div class="row mb-4 align-items-center">
      <div class="col-12 d-flex justify-content-between align-items-center">
        <h3 class="fw-bold text-primary">Our Categories</h3>
        <router-link :to="{ name: 'AddCategory' }">
          <button class="btn btn-success fw-bold px-4 shadow-sm">
            <i class="fas fa-plus me-2"></i> Add Category
          </button>
        </router-link>
      </div>
    </div>

    <!-- Category Cards -->
    <div class="row">
      <div v-for="category in categoriesWithProductCount" :key="category.id" class="col-xl-4 col-md-6 col-12 pt-3 d-flex">
        <CategoryBox :category="category" :productCount="category.productCount" @deleteCategory="confirmDeleteCategory" />
      </div>
    </div>

    <!-- Confirm Dialog for Category Deletion -->
    <ConfirmDialog ref="confirmDialog"
                   title="Delete Confirmation"
                   :itemType="'category'"
                   :itemName="deleteItemName"
                   :itemId="deleteItemId"
                   @confirmed="deleteCategory"
    />
  </div>
</template>

<script>
import axios from "axios";
import CategoryBox from "@/components/Category/CategoryBox.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  name: "Category",
  components: { CategoryBox, ConfirmDialog },
  props: ["baseURL", "categories"],
  computed: {
    // ✅ Compute product count once in the parent component
    categoriesWithProductCount() {
      return this.categories.map(category => ({
        ...category,
        productCount: category.products ? category.products.length : 0
      }));
    }
  },
  data() {
    return {
      deleteItemId: null,
      deleteItemName: ""
    };
  },
  methods: {
    confirmDeleteCategory(category) {
      this.deleteItemId = category.id;
      this.deleteItemName = category.categoryName;
      this.$refs.confirmDialog.openModal();
    },
    async deleteCategory(categoryId) {
      try {
        const response = await axios.delete(`${this.baseURL}category/delete/${categoryId}`);
        if (response.status === 200) {
          this.$emit("fetchData");
        } else {
          console.error("⚠️ Failed to delete category.");
        }
      } catch (err) {
        console.error("❌ Error deleting category:", err);
      }
    }
  }
};
</script>
