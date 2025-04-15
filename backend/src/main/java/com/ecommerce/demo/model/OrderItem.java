package com.ecommerce.demo.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="order_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="purchase_price")
    private double purchasePrice; // the price per item

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
