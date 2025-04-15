package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.checkout.CheckoutItemDto;
import com.ecommerce.demo.dto.order.OrderDto;
import com.ecommerce.demo.dto.order.OrderItemDto;
import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.OrderItem;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.CartRepo;
import com.ecommerce.demo.repository.OrderRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartRepo cartRepo;
    @Value("${BASE_URL}")
    private String baseURL; // front end url

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Autowired
    private OrderRepo orderRepo;

    public OrderService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        // https://docs.stripe.com/api/checkout/sessions/create
        // success and failure urls
        String successURL = baseURL + "payment/success";
        String failureURL = baseURL + "payment/failed";
        Stripe.apiKey = apiKey;
        
        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();
        for(CheckoutItemDto checkoutItemDto: checkoutItemDtoList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDto));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .setSuccessUrl(successURL)
                .addAllLineItem(sessionItemList)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("eur")
                .setUnitAmount((long)(checkoutItemDto.getPrice()*100)) // Stripe requires prices to be sent as an integer in the smallest currency unit to avoid floating-point precision issues.
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getProductName())
                                .build()
                ).build();
    }

    @Transactional
    public void placeOrder(User user, List<Cart> cartItems) {
        Order order = createNewOrder(user);
        double totalCost = 0;

        for(Cart cartItem: cartItems) {
           OrderItem orderItem = createOrderItemFromCart(cartItem, order);
           totalCost += orderItem.getPurchasePrice() * orderItem.getQuantity();
           order.getOrderItemList().add(orderItem);
        }
        order.setTotalCost(totalCost);
        orderRepo.save(order); // CascadeType.ALL ensures order items are saved when saving the order

        cartRepo.deleteAllByUser(user);
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepo.findByUserOrderByOrderDateDesc(user);
    }

    private Order createNewOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderItemList(new ArrayList<>());
        return order;
    }

    private OrderItem createOrderItemFromCart(Cart cartItem, Order order) {
        OrderItem item = new OrderItem();
        item.setProduct(cartItem.getProduct());
        item.setQuantity(cartItem.getQuantity());
        item.setPurchasePrice(cartItem.getProduct().getPrice());
        item.setOrder(order);
        return item;
    }

    public OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductName(orderItem.getProduct().getName());
        orderItemDto.setImageURL(orderItem.getProduct().getImageURL());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPurchasePrice(orderItem.getPurchasePrice());
        orderItemDto.setLineTotal(orderItem.getPurchasePrice() * orderItem.getQuantity());
        return orderItemDto;
    }

    public OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTotalCost(order.getTotalCost());
        orderDto.setOrderDate(order.getOrderDate());
        List<OrderItemDto> orderItemDtoList = order.getOrderItemList().stream().map(this::mapToOrderItemDto).collect(Collectors.toList());
        orderDto.setItems(orderItemDtoList);
        return orderDto;
    }
}
