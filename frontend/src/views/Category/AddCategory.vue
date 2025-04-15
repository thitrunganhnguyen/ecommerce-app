<template>
<div class="container mt-5">
<div class="row justify-content-center">
  <div class="col-md-8">
    <div class="card shadow-lg">
      <div class="card-header bg-primary text-white text-center">
        <h4 class="mb-0">Add New Category</h4>
      </div>
      <div class="card-body">
        <form @submit.prevent="validateForm">
          <!-- Name Field -->
          <div class="form-group mb-3">
            <label class="fw-bold">Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" v-model="categoryName" :class="{ 'is-invalid': v$.categoryName.$error }" placeholder="Enter category name">
            <div class="invalid-feedback" v-if="v$.categoryName.$error">Name must be at least 3 characters</div>
          </div>
          <!-- Description Field (Optional) -->
          <div class="form-group mb-3">
            <label class="fw-bold">Description (Optional)</label>
            <textarea class="form-control" v-model="description" rows="3" placeholder="Enter product description"></textarea>
          </div>
          <!-- Image URL Field (Optional) -->
          <div class="form-group mb-3">
            <label class="fw-bold">Image URL (Optional)</label>
            <input type="text" class="form-control" v-model="imageUrl" placeholder="Enter image URL">
          </div>
          <!-- Submit Button -->
          <div class="text-center">
            <button type="submit" class="btn btn-primary px-4 py-2 fw-bold" style="transition: 0.3s;">
              Add Category
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
import {useVuelidate} from "@vuelidate/core";
import {minLength, required} from "@vuelidate/validators";


export default {
  setup() {
    return { v$: useVuelidate() }; // Enables validation
  },
  props: ["baseURL", "categories"],
  data() {
    return {
      categoryName: "",
      description: "",
      imageUrl: ""
    }
  },
  validations() {
    return {
      categoryName: { required, minLength: minLength(3) },
    };
  },
  methods: {
    async validateForm() {
      const isValid = await this.v$.$validate(); // Validate form before submission
      if (!isValid) return; // Stop submission if the form is invalid

      this.addCategory();
    },
    async addCategory() {
      const newCategory = {
        categoryName: this.categoryName,
        description: this.description,
        imageUrl: this.imageUrl
      };
      await this.$axios.post(this.baseURL + "category/create", newCategory)
          .then(() => {
            this.$emit("fetchData");
            this.$router.push({name: 'Category'});
            this.$sweetAlert({
              text: "Category added successfully",
              icon: "success"
            })
          })
          .catch(err => {
            console.log("err", err);
          })
    }
  },
  mounted() {
    if(!localStorage.getItem('token')) {
      this.$router.push({name: 'SignIn'});
    }
  }
}
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