package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.Review;
import com.shop.ecommerce.repository.ReviewRepository;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.repository.ProductRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review addReview(Review review, String username, Long productId) {
        var user = userRepository.findByUsername(username).orElseThrow();
        var product = productRepository.findById(productId).orElseThrow();

        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(java.time.LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, Review updatedReview, String username, boolean isAdmin) {
        Review existing = reviewRepository.findById(reviewId).orElseThrow();

        if (!isAdmin && !existing.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can edit only your own reviews");
        }
        existing.setComment(updatedReview.getComment());
        existing.setRating(updatedReview.getRating());
        return reviewRepository.save(existing);
    }

    public void deleteReview(Long reviewId, String username, boolean isAdmin) {
        Review existing = reviewRepository.findById(reviewId).orElseThrow();
        if (!isAdmin && !existing.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can delete only your own reviews");
        }
        reviewRepository.delete(existing);
    }
}
