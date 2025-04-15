<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid d-flex align-items-center justify-content-between">

    <!-- Logo -->
      <router-link class="navbar-brand d-flex align-items-center" :to="{ name: 'Home' }">
        <img id="logo" src="https://i.pinimg.com/736x/46/ba/81/46ba816887a076075803d6ae81f441fd.jpg" />
      </router-link>

      <!-- Search Bar -->
      <!-- the submit event will no longer reload the page -->
      <form class="d-flex align-items-center mx-auto"
            style="width: 60%;"
            @submit.prevent="emitSearch">
        <div class="input-group">
              <input v-model="searchText"
                     @input="debouncedSearch"
                     type="text"
                     class="form-control"
                     style="height: 40px;"
                     placeholder="Search Products"
                     aria-label="Search">
              <button class="btn btn-warning" type="submit" style="height: 40px; margin-bottom: 0px">
                <i class="bi bi-search fs-5"></i>
              </button>
            </div>
      </form>

      <!-- Navigation Links -->
      <ul class="navbar-nav d-flex align-items-center">
      <!-- Browse Dropdown -->
        <li class="nav-item dropdown mx-2">
          <a class="nav-link dropdown-toggle text-light" href="#" id="browseDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            Browse
          </a>
          <ul class="dropdown-menu" aria-labelledby="browseDropdown">
            <li><router-link class="dropdown-item" :to="{ name: 'Home' }">Home</router-link></li>
            <li><router-link class="dropdown-item" :to="{ name: 'Category' }">Category</router-link></li>
            <li><router-link class="dropdown-item" :to="{ name: 'AdminProduct' }">Product</router-link></li>
          </ul>
        </li>

        <!-- Accounts Dropdown -->
        <li class="nav-item dropdown mx-2">
          <a class="nav-link dropdown-toggle text-light" href="#" id="accountDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            Accounts
          </a>
          <ul class="dropdown-menu" aria-labelledby="accountDropdown">
            <li v-if="token"><router-link class="dropdown-item" :to="{ name: 'WishList' }">Wish list</router-link></li>
            <li v-if="!token"><router-link class="dropdown-item" :to="{ name: 'SignUp' }">Sign up</router-link></li>
            <li v-if="!token"><router-link class="dropdown-item" :to="{ name: 'SignIn' }">Sign in</router-link></li>
            <li v-if="token"><a class="dropdown-item" href="#" @click="signout">Sign out</a></li>
          </ul>
        </li>

        <!-- Order -->
        <li class="nav-item mx-2" v-if="token">
          <router-link class="nav-link text-light d-flex" :to="{ name: 'PaymentSuccess' }">
            My Orders
          </router-link></li>

        <!-- Cart Icon -->
        <li class="nav-item mx-2">
          <div id="cart" class="position-relative d-flex align-items-center">
            <router-link class="text-light position-relative" :to="{ name: 'Cart' }">
              <i class="fa fa-shopping-cart fs-4"></i> <!-- Adjusted size -->
              <span id="nav-cart-count"
                    class="position-absolute top-0 start-100 translate-middle badge bg-danger rounded-circle">
                {{ cartCount }}
              </span>
            </router-link>
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>



<script>
import swal from "sweetalert";

function debounce(func, delay) {
  let timerId;
  return function() {
    clearTimeout(timerId);
    timerId = setTimeout(() => {
      func()
    }, delay);
  }
}

export default {
  name: "Navbar",
  props: ["cartCount"],
  data() {
    return {
      token: null,
      searchText: '',
      debouncedSearch: null
    };
  },
  methods: {
    emitSearch() {
      this.$emit('search', this.searchText.trim());
    },
    signout() {
      localStorage.removeItem("token");
      this.token = null;
      swal({
        text: "Logged you out. Visit again",
        icon: "success",
      });
      this.$emit("resetCartCount");
      this.$router.push({ name: "Home" });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.debouncedSearch = debounce(this.emitSearch, 2000);
  },
};
</script>
<style scoped>
/* Set Navbar Height */
.navbar {
  height: 120px; /* Adjusted for better spacing */
  display: flex;
  align-items: center;
}

#logo {
  width: 70px;
  height: auto;
  display: flex;
  align-items: center;
}

/* Ensure Search Bar & Button are the Same Height */
.search-bar {
  width: 60%;
}

.input-group {
  display: flex;
  align-items: stretch; /* Ensures both elements stretch equally */
}

.input-group input {
  height: 40px !important; /* Match input and button height */
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  font-size: 1rem;
  flex-grow: 1; /* Ensure input fills remaining space */
  border: 1px solid #ccc; /* Match border style */
}

.input-group button {
  height: 40px !important; /* EXACT same height as input */
  width: 40px; /* Square button */
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0; /* Remove any extra padding */
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  border: 1px solid #ccc; /* Ensure border matches input */
  border-left: none; /* Remove duplicate border */
}

.input-group button i {
  font-size: 1rem; /* Adjust icon size */
  display: flex;
  align-items: center;
  justify-content: center;
}


/* Fix Cart Icon Size */
#cart i {
  font-size: 1.5rem;
}

/* Fix Cart Count Badge Position */
#nav-cart-count {
  font-size: 12px;
  padding: 2px 5px;
  transform: translate(50%, -50%);
}
</style>
