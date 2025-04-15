package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.exceptions.AuthentificationFailException;
import com.ecommerce.demo.exceptions.ProductNotExistException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.service.AuthenticationTokenService;
import com.ecommerce.demo.service.CartService;
import com.ecommerce.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto, @RequestParam("token") String token)
            throws AuthentificationFailException, ProductNotExistException {

        authenticationTokenService.authenticate(token);
        User user = authenticationTokenService.getUser(token);
        Product product = productService.getProductById(addToCartDto.getProductId());

        cartService.addToCart(addToCartDto, product, user);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthentificationFailException{
        authenticationTokenService.authenticate(token);
        User user = authenticationTokenService.getUser(token);
        CartDto cartDtos =  cartService.listCartItems(user);
        return new ResponseEntity<>(cartDtos, HttpStatus.OK);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Integer cartItemId, @RequestBody AddToCartDto cartDto,
                                                      @RequestParam("token") String token) {
        authenticationTokenService.authenticate(token);
        User user = authenticationTokenService.getUser(token);
        Product product = productService.getProductById(cartDto.getProductId());

        cartService.updateCartItem(cartItemId, cartDto, product, user);
        return new ResponseEntity<>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Integer cartItemId, @RequestParam("token") String token) {

        authenticationTokenService.authenticate(token);
        int userId = authenticationTokenService.getUser(token).getId();

        cartService.deleteCartItem(cartItemId, userId);

        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }

}
