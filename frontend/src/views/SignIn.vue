<template>
  <div class="container">
    <div class="row">
      <div class="col-12 justify-content-center d-flex flex-row pt-5">
        <div id="signin" class="flex-item border">
          <h2 class="pt-4">Sign In</h2>
          <form class="pt-4 pl-4 pr-4" @submit="signin">
            <div class="form-group">
              <label>Email</label>
              <input type="email" class="form-control" v-model="email">
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" class="form-control" v-model="password">
            </div>
            <button type="submit" class="btn btn-primary mt-2 py-0">Continue</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import swal from "sweetalert";
export default {
  props: ["baseURL"],
  data() {
    return {
      email: null,
      password: null,
    }
  },
  methods: {
    async signin(e) {
      console.log("test");
      e.preventDefault();
        const body = {
          email: this.email,
          password: this.password
        };
        await axios.post(`${this.baseURL}user/signin`, body)
            .then((res) => {
              localStorage.setItem("token", res.data.token); // Need token to get the user's wishlist.
              swal({
                text: "Login successful",
                icon: "success",
              });
              this.$emit("fetchData"); // update cart count
              this.$router.push({name: "Home"});
            })
            .catch(err => console.log('err', err));
      }
    }
}
</script>
<style scoped>
.btn-primary {
  background-color: #f0c14b;
  color: black;
}
@media screen {
  #signin {
    width: 40%;
  }

}
</style>