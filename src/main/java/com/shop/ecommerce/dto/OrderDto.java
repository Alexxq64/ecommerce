package com.shop.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private LocalDateTime createdAt;
    private String status;
    private List<OrderItemDto> items;
    private Double totalAmount;
}
