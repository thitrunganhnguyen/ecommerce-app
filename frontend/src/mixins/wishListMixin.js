export default {
    props: ["baseURL"],
    data(){
        return {
            token: null,
            products: null,
        }
    },
    methods: {
        fetchWishList() {
            this.$axios.get(`${this.baseURL}wishlist/${this.token}`)
                .then(res => {
                    this.products = res.data;
                })
                .catch(err => {
                    console.log("err", err);
                })
        }
    },
    mounted(){
        this.token = localStorage.getItem("token");
        this.fetchWishList();
    }
};