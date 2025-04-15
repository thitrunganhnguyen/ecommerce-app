package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.dto.cart.CartItemDto;
import com.ecommerce.demo.exceptions.CartItemNotExistException;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    public static CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }

    public void addToCart(AddToCartDto addToCartDto, Product product, User user) {
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepo.save(cart);
    }

    public CartDto listCartItems(User user) {
        // List<Cart> cartList = cartRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<Cart> cartList = cartRepo.findAllByUserOrderByProduct_NameAsc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;

        for(Cart cart: cartList) {
            CartItemDto cartItem = getDtoFromCart(cart);
            cartItems.add(cartItem);
            totalCost += cartItem.getQuantity() * cart.getProduct().getPrice();
        }
        return new CartDto(cartItems, totalCost);
    }

    public List<Cart> getUserCartItems(User user) {
        return cartRepo.findAllByUserOrderByCreatedDateDesc(user);
    }

    public void updateCartItem(Integer cartItemId, AddToCartDto cartDto, Product product, User user) {

        // Cart and cartItemDto have the same id
        Optional<Cart> optionalCart = cartRepo.findById(cartItemId);
        if(optionalCart.isEmpty()) {
            throw new CustomException("cart item id is invalid: " + cartItemId);
        }

        Cart cart = optionalCart.get();
        cart.setProduct(product);
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());

        cartRepo.save(cart);
    }

    public void deleteCartItem(Integer cartItemId, int userId) {

        // Cart and cartItemDto have the same id
       if(!cartRepo.existsById(cartItemId)) {
           throw new CartItemNotExistException("Cart id is invalid : " + cartItemId);
       }

       Optional<Cart> cart = cartRepo.findByIdAndUserId(cartItemId, userId);

        if(cart.isEmpty()) {
            throw new CustomException("cart item does not belong to user: " + cartItemId);
        }
        cartRepo.deleteById(cartItemId);
    }

}
