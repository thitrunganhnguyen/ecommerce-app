<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <!-- Product Image -->
      <div class="col-lg-5 col-md-6 col-12 text-center">
        <img :src="product.imageURL" class="img-fluid rounded shadow" alt="Product Image">
      </div>

      <!-- Product Details -->
      <div class="col-lg-6 col-md-6 col-12 pt-md-0 pt-4">
        <h2 class="fw-bold">{{ product.name }}</h2>
        <h5 class="category text-muted">{{ category.categoryName }}</h5>
        <h4 class="fw-bold text-primary">{{ product.price }} €</h4>
        <p class="text-muted">{{ product.description || "No description available." }}</p>

        <!-- Quantity and Add to Cart -->
        <div class="d-flex align-items-center mb-3">
          <div class="input-group me-3" style="max-width: 180px;">
            <span class="input-group-text bg-light">Quantity</span>
            <input class="form-control text-center" type="number" v-model="quantity" min="1" style="max-width: 90px;">
          </div>
          <button id="add-to-cart-button" class="btn btn-warning fw-bold px-4 mb-0 d-flex align-items-center" @click="addToCart">
            <i class="fas fa-shopping-cart me-2" style="vertical-align: middle;"></i>
            In den Warenkorb legen
          </button>
        </div>

        <!-- Wishlist Button -->
        <button id="wishlist-button" class="btn btn-light text-dark fw-bold px-3 d-flex align-items-center"
                @click="toggleWishList">
          <i class="fas fa-heart me-2" :class="{ 'text-danger': inWishlist }"></i>
          {{ inWishlist ? "Remove from Wishlist" : "Add to Wishlist" }}
        </button>

        <!-- Features Section -->
        <div class="features mt-4">
          <h5 class="fw-bold">Key Features</h5>
          <ul class="list-unstyled">
            <li><i class="fas fa-check text-success me-2"></i> So many books, so little time.</li>
            <li><i class="fas fa-check text-success me-2"></i> Be who you are and say what you feel.</li>
            <li><i class="fas fa-check text-success me-2"></i> You only live once, but if you do it right, once is enough.</li>
            <li><i class="fas fa-check text-success me-2"></i> In three words: it goes on.</li>
            <li><i class="fas fa-check text-success me-2"></i> If you tell the truth, you don't have to remember anything.</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import swal from "sweetalert";

export default {
  props: ["baseURL", "products", "categories"],
  data() {
    return {
      product: {},
      category: {},
      quantity: 1,
      inWishlist: false,
      token: localStorage.getItem("token")
    };
  },
  methods: {
    toggleWishList() {
      if (!this.token) {
        swal({ text: "Please log in to manage your wishlist", icon: "error" });
        return;
      }

      if (this.inWishlist) {
        // Remove from wishlist
        axios.delete(`${this.baseURL}wishlist/delete?productId=${this.product.id}&token=${this.token}`)
            .then(res => {
              if (res.status === 200) {
                this.inWishlist = false;
                swal({ text: "Removed from Wishlist", icon: "success" });
              }
            })
            .catch(err => console.log("Wishlist removal error", err));
      } else {
        // Add to wishlist
        axios.post(`${this.baseURL}wishlist/add?token=${this.token}`, { id: this.product.id })
            .then(res => {
              if (res.status === 201) {
                this.inWishlist = true;
                swal({ text: "Added to Wishlist", icon: "success" });
              }
            })
            .catch(err => console.log("Wishlist addition error", err));
      }
    },
    addToCart() {
      if (!this.token) {
        swal({ text: "Please log in to add items to your cart", icon: "error" });
        return;
      }
      axios.post(`${this.baseURL}cart/add?token=${this.token}`, {
        productId: this.product.id,
        quantity: this.quantity
      })
          .then(res => {
            if (res.status == 201) {
              swal({ text: "Product added to cart", icon: "success" });
              this.$emit("fetchData");
            }
          })
          .catch(err => console.log("err", err));
    }
  },
  mounted() {
    this.id = this.$route.params.id;
    this.product = this.products.find(product => product.id == this.id);
    this.category = this.categories.find(category => category.id == this.product.categoryId);

    // Check if product is in wishllist
    if(this.token) {
      axios.get(`${this.baseURL}wishlist/${this.token}`)
          .then(res => {
            this.inWishlist = res.data.some(item => item.id === this.product.id);
            console.log("inWishList " + this.inWishlist);
          })
          .catch(err => {
            console.log("Wishlist fetch error", err);
          })
    }
  }
};
</script>

<style scoped>
/* Category Style */
.category {
  font-weight: 400;
}

/* Wishlist Button */
#wishlist-button {
  background-color: #f8f9fa;
  border: 1px solid #ccc;
  transition: 0.3s;
}

#wishlist-button:hover {
  background-color: #e9ecef;
  border-color: #bbb;
}

/* Add to Cart Button */
#add-to-cart-button {
  background-color: #febd69;
  transition: 0.3s;
}

#add-to-cart-button:hover {
  background-color: #e6a847;
}

/* Product Image */
img {
  border-radius: 10px;
  max-height: 400px;
  object-fit: contain;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Quantity Input */
.input-group {
  border-radius: 5px;
}

.input-group-text {
  font-weight: bold;
  border-radius: 5px 0 0 5px;
}

.form-control {
  text-align: center;
  border-radius: 0 5px 5px 0;
}

/* Features List */
.features ul {
  padding-left: 0;
}

.features li {
  margin-bottom: 5px;
  font-size: 16px;
}

.features i {
  font-size: 18px;
}
</style>
