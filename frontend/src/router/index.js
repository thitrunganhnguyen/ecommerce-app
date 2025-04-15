import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AddCategory from "@/views/Category/AddCategory";
import Category from "@/views/Category/Category";
import Admin from "@/views/Admin";
import Product from "@/views/Product/Product.vue";
import AddProduct from "@/views/Product/AddProduct.vue";
import EditCategory from "@/views/Category/EditCategory.vue";
import EditProduct from "@/views/Product/EditProduct.vue";
import ShowDetails from "@/views/Product/ShowDetails.vue";
import ListProducts from "@/views/Category/ListProducts.vue";
import SignUp from "@/views/SignUp.vue";
import SignIn from "@/views/SignIn.vue";
import WishList from "@/views/Product/WishList.vue";
import Cart from "@/views/Cart.vue";
import Success from "@/views/payment/Success.vue";
import Failed from "@/views/payment/Failed.vue";
import Checkout from "@/views/Checkout/Checkout.vue";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  // admin home page
  {
    path: '/admin',
    name: 'Admin',
    component: Admin
  },
  {
    path: '/admin/category',
    name: 'Category',
    component: Category
  },
  {
    path: '/admin/category/add',
    name: 'AddCategory',
    component: AddCategory
  },
  {
    path: '/admin/category/:id',
    name: 'EditCategory',
    component: EditCategory
  },
  {
    path: '/admin/category/delete/:id',
    name: 'EditCategory',
    component: EditCategory
  },
    // list all products of category
  {
    path: '/category/show/:id',
    name: 'ListProducts',
    component: ListProducts
  },
  {
    path: '/admin/product',
    name: 'AdminProduct',
    component: Product
  },
  {
    path: '/admin/product/new',
    name: 'AddProduct',
    component: AddProduct
  },
  {
    path: '/admin/product/:id',
    name: 'EditProduct',
    component: EditProduct
  },
    // show details of product
  {
    path: '/product/show/:id',
    name: 'ShowDetails',
    component: ShowDetails
  },
  {
    path: '/signup',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/signin',
    name: 'SignIn',
    component: SignIn
  },
  {
    path: '/wishlist',
    name: 'WishList',
    component: WishList
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart
  },
  {
    path: '/payment/success',
    name: 'PaymentSuccess',
    component: Success
  },
  {
    path: '/payment/failed',
    name: 'PaymentFail',
    component: Failed
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: Checkout
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
