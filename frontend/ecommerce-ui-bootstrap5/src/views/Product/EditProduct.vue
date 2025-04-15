<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit Product</h4>
      </div>
    </div>
    <div class="row">
      <div class="col-3"></div>
      <div class="col-6"> <!--The form occupies 6 columns in the center of the row-->
        <!--
        Initially, product = null, so Vue does not render the form.
        Since v-if completely removes elements from the DOM when the condition is false, the form is not created at all. -->
        <form v-if="product">
          <div class="form-group">
            <label>Category</label>
            <select class="form-control" v-model="product.categoryId" required>
              <option v-for="category in categories" :key="category.id"
                      :value="category.id">{{ category.categoryName }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Name</label>
            <input type="text" class="form-control" v-model="product.name" required>
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea type="text" class="form-control" v-model="product.description" required />
          </div>
          <div class="form-group">
            <label>Image Url</label>
            <input type="text" class="form-control" v-model="product.imageURL" required>
          </div>
          <div class="form-group">
            <label>Price</label>
            <input type="number" class="form-control" v-model="product.price" required>
          </div>
          <button type="button" class="btn btn-primary" @click="editProduct">Submit</button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>
<script>

import axios from "axios";
import swal from "sweetalert";
export default  {
  props: ["categories", "baseURL", "products"],
  data() {
    return {
      product: null,
      id: null
    }
  },
  methods: {
    async editProduct() {
      await axios.post(this.baseURL + "product/update/" + this.id, this.product)
          .then(() => {
            this.$emit("fetchData");
            this.$router.push({name: "AdminProduct"});
            swal( {
              text: "Product has been updated successfully",
              icon: "success"
            })
          }).catch(err => console.log("err", err))
    }
  },
  mounted() {
    this.id = this.$route.params.id;
    this.product = this.products.find(product => product.id == this.id);
  }

}
</script>