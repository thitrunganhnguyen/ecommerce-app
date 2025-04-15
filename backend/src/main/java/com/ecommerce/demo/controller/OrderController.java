package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.checkout.CheckoutItemDto;
import com.ecommerce.demo.dto.checkout.StripeResponse;
import com.ecommerce.demo.dto.order.OrderDto;
import com.ecommerce.demo.exceptions.AuthentificationFailException;
import com.ecommerce.demo.exceptions.ProductNotExistException;
import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.service.AuthenticationTokenService;
import com.ecommerce.demo.service.CartService;
import com.ecommerce.demo.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;


    // stripe session checkout api
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token)
            throws AuthentificationFailException {

        authenticationTokenService.authenticate(token);
        User user = authenticationTokenService.getUser(token);
        List<Cart> cartItems = cartService.getUserCartItems(user);
        orderService.placeOrder(user, cartItems);
        return new ResponseEntity<>(new ApiResponse(true, "Order placed successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDto>> getUserOrders(@RequestParam("token") String token) {
        authenticationTokenService.authenticate(token);
        User user = authenticationTokenService.getUser(token);
        List<Order> userOrders = orderService.getOrdersForUser(user);
        List<OrderDto> orderDtoList = userOrders
                                    .stream()
                                    .map(orderService::mapToOrderDto)
                                    .collect(Collectors.toList());

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }
}
