<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card shadow-lg">
          <div class="card-header bg-primary text-white text-center">
            <h4 class="mb-0">Add New Product</h4>
          </div>
          <div class="card-body">
            <form @submit.prevent="validateForm">
              <!-- Category Field -->
              <div class="form-group mb-3">
                <label class="fw-bold">Category <span class="text-danger">*</span></label>
                <select v-model="categoryId" class="form-control" :class="{ 'is-invalid': v$.categoryId.$error }">
                  <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.categoryName }}
                  </option>
                </select>
                <div class="invalid-feedback" v-if="v$.categoryId.$error">Category is required</div>
              </div>

              <!-- Name Field -->
              <div class="form-group mb-3">
                <label class="fw-bold">Name <span class="text-danger">*</span></label>
                <input type="text" class="form-control" v-model="name" :class="{ 'is-invalid': v$.name.$error }" placeholder="Enter product name">
                <div class="invalid-feedback" v-if="v$.name.$error">Name must be at least 3 characters</div>
              </div>

              <!-- Description Field (Optional) -->
              <div class="form-group mb-3">
                <label class="fw-bold">Description (Optional)</label>
                <textarea class="form-control" v-model="description" rows="3" placeholder="Enter product description"></textarea>
              </div>

              <!-- Image URL Field (Optional) -->
              <div class="form-group mb-3">
                <label class="fw-bold">Image URL (Optional)</label>
                <input type="text" class="form-control" v-model="imageURL" placeholder="Enter image URL">
              </div>

              <!-- Price Field -->
              <div class="form-group mb-3">
                <label class="fw-bold">Price <span class="text-danger">*</span></label>
                <input type="number" step="0.01" class="form-control" v-model="price" :class="{ 'is-invalid': v$.price.$error }" placeholder="Enter price (e.g., 9.99)">
                <div class="invalid-feedback" v-if="v$.price.$error">Price must be a number and at least 0.01</div>
              </div>

              <!-- Submit Button -->
              <div class="text-center">
                <button type="submit" class="btn btn-primary px-4 py-2 fw-bold" style="transition: 0.3s;">
                  Add Product
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useVuelidate } from "@vuelidate/core";
import { required, minLength, numeric, minValue } from "@vuelidate/validators";

export default {
  setup() {
    return { v$: useVuelidate() }; // Enables validation
  },
  props: ["categories", "baseURL"],
  data() {
    return {
      categoryId: null,
      name: "",
      description: "", // Optional
      imageURL: "", // Optional
      price: null
    };
  },
  validations() {
    return {
      categoryId: { required },
      name: { required, minLength: minLength(3) },
      price: { required, numeric, minValue: minValue(0.01) } // Allows decimal values like 0.99
    };
  },
  methods: {
    async validateForm() {
      const isValid = await this.v$.$validate(); // Validate form before submission
      if (!isValid) return; // Stop submission if the form is invalid

      this.addProduct();
    },
    addProduct() {
      this.$axios.post(this.baseURL + "product/add", {
        categoryId: this.categoryId,
        name: this.name,
        description: this.description || null, // Ensure optional fields are null if empty
        imageURL: this.imageURL || null, // Ensure optional fields are null if empty
        price: this.price
      })
          .then(() => {
            this.$emit("fetchData"); // Refresh product list
            this.$router.push({ name: "AdminProduct" }); // Navigate to AdminProduct page
            this.$sweetAlert({ text: "Product added", icon: "success" }); // Show success message
          })
          .catch(err => console.log("err", err));
    }
  }
};
</script>

<style scoped>
/* Add padding for form */
.card {
  border-radius: 10px;
  overflow: hidden;
}

.card-header {
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.btn-primary:hover {
  background-color: #004085;
  transform: scale(1.05);
}

.form-control {
  border-radius: 5px;
}

.invalid-feedback {
  font-size: 0.875rem;
}
</style>
