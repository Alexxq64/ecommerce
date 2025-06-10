package com.shop.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long id;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private String username;
    private Long productId;
}
