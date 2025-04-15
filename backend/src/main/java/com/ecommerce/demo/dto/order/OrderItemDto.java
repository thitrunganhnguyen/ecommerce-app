package com.ecommerce.demo.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {
    private String productName;
    private String imageURL;
    private int quantity;
    private double purchasePrice;
    private double lineTotal;
}
