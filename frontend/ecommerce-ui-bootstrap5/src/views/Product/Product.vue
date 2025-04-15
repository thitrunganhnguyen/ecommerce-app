<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h3 class="pt-3">Our Products</h3>
        <router-link :to="{name: 'AddProduct'}" class="add-product-link">
          <button class="btn btn-success add-product-btn">
            <i class="bi bi-plus-circle me-1"></i>Add Product</button>
        </router-link>
      </div>
    </div>

    <div class="row">
      <!-- Display all the products in productbox component -->
      <div v-for="product of products" :key="product.id"
           class="col-xl-4 col-md-6 col-12 pt-3 d-flex">
        <ProductBox :product="product" @deleteProduct="confirmDeleteProduct"></ProductBox>
      </div>
    </div>

    <!-- ✅ ConfirmDialog to Confirm Deletion -->
    <ConfirmDialog ref="confirmDialog"
                   title="Delete Confirmation"
                   :itemType="'product'"
                   :itemName="deleteItemName"
                   :itemId="deleteItemId"
                   @confirmed="deleteProduct"
    />

  </div>
</template>
<script>
import ProductBox from "@/components/Product/ProductBox.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";
import axios from "axios";

export default {
  components: {ConfirmDialog, ProductBox},
  props:["products", "baseURL"],
  data() {
    return {
      deleteItemId: null, // Store the product ID for deletion
      deleteItemName: "" // Store the product name for deletion
    };
  },
  methods: {
    // Show the ConfirmationDialog with product details
    confirmDeleteProduct(product)  {
      this.deleteItemId = product.id;
      this.deleteItemName = product.name;
      this.$refs.confirmDialog.openModal();
    },
    // Delete product via API after Confirmation
    async deleteProduct(productId) {
      console.log(`🔄 Attempting to delete category with ID: ${productId}`);

      try {
        const response = await axios.delete(`${this.baseURL}product/delete/${productId}`);
        console.log("✅ API Response:", response);

        if (response.status === 200) {
          // ✅ Emit event to update categories in AppView.vue
          this.$emit("fetchData");
          console.log(`✅ Product ${productId} deleted successfully`);
        } else {
          console.error(`⚠️ Product deletion failed. Response:`, response);
        }
      } catch (err) {
        console.error("❌ Error deleting product:", err);
      }
    }

  }
}
</script>
<style scoped>
.add-product-link {
  display: inline-block;
  margin-top: 10px;
  float: right;
}
.add-product-btn {
  font-size: 1rem;
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 500;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  transition: all 0.2s ease-in-out;
}

.add-product-btn:hover {
  background-color: #218838; /* darker green */
  transform: translateY(-2px);
}
</style>