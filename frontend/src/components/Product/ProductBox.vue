<template>
  <div class="card w-100 h-100  shadow-sm border-0 rounded-3 overflow-hidden" style="margin-top: 10px">
    <!-- Fix Bootstrap 5 Responsive Image -->
    <div class="ratio ratio-16x9">
      <img class="card-img-top" :src="product.imageURL" alt="Card image cap">
    </div>

    <div class="card-body">
      <router-link :to="{name: 'ShowDetails', params: {id: product.id}}" class="text-decoration-none">
        <h5 class="card-title  text-dark fw-bold">{{ product.name }}</h5>
      </router-link>
      <p class="card-text text-muted">
        {{ product.description ? product.description.substring(0, 65) + '...' : 'No description available' }}
      </p>

      <h6 class="fw-bold">{{ product.price}} €</h6>

      <!-- Show button only on Product page, disable in Home page -->
      <div class="d-flex align-items-center" v-if="$route.name == 'AdminProduct'">

        <router-link :to="{name: 'EditProduct', params: {id: product.id}}">
          <button class="btn btn-primary  btn-sm me-2">Edit</button>
        </router-link>
        <button class="btn btn-danger btn-sm" @click="requestDelete">
          Delete
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductBox",
  props: ["product"],
  methods: {
    requestDelete() {
      console.log(`Requesting delete for category Id: ${this.product.id}`);
      this.$emit("deleteProduct", this.product); // Emit event to parent (Product.vue)

    }
  }
}
</script>


