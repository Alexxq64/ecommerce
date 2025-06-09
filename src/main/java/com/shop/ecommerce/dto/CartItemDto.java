package com.shop.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}
