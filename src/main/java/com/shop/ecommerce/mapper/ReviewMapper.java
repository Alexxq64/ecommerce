package com.shop.ecommerce.mapper;

import com.shop.ecommerce.dto.ReviewDto;
import com.shop.ecommerce.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUsername(review.getUser() != null ? review.getUser().getUsername() : null);
        dto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
        return dto;
    }

    public Review toEntity(ReviewDto dto) {
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        // id, user и product устанавливаются в сервисе
        return review;
    }
}
