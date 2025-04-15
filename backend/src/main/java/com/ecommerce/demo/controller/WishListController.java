package com.ecommerce.demo.controller;
import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.product.ProductDto;
import com.ecommerce.demo.exceptions.ProductNotExistException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.service.AuthenticationTokenService;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;
    @Autowired
    AuthenticationTokenService authenticationTokenService;

    @Autowired
    ProductService productService;

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) {
        // authenticate the token
        authenticationTokenService.authenticate(token);

        int userId = authenticationTokenService.getUser(token).getId();

        List<WishList> wishLists = wishListService.readWishList(userId);

        List<ProductDto> productDtos =  new ArrayList<>();

        for(WishList wishList : wishLists) {
            productDtos.add(ProductService.getDtoFromProduct(wishList.getProduct()));
        }
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    // save product as wishlist item
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product, @RequestParam("token") String token) {
        // authenticate the token
        authenticationTokenService.authenticate(token);

        // find the user
        User user = authenticationTokenService.getUser(token);

        // save the item in wishlist
        WishList wishList = wishListService.getWishListByUserAndProduct(user, product);
        if(Objects.isNull(wishList)) {
            wishList = new WishList(user, product);
            wishListService.createWishList(wishList);
            ApiResponse apiResponse = new ApiResponse(true, "Added to wishlist");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        else {
            ApiResponse apiResponse = new ApiResponse(true, "Product already in wishlist");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteProductFromWishList(@RequestParam("productId") int productId,
                                                                 @RequestParam("token") String token) {
        // Authenticate the token
        authenticationTokenService.authenticate(token);

        // Get the user from the token
        User user = authenticationTokenService.getUser(token);

        try {
            // Fetch the product from the database (will throw exception if not found)
            Product product = productService.getProductById(productId);

            // Fetch the wishlist entry
            WishList wishList = wishListService.getWishListByUserAndProduct(user, product);

            if (wishList != null) {
                // Remove from wishlist
                wishListService.deleteWishList(wishList);
                return new ResponseEntity<>(new ApiResponse(true, "Product removed from wishlist"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Product not found in wishlist"), HttpStatus.NOT_FOUND);
            }

        } catch (ProductNotExistException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }





}
