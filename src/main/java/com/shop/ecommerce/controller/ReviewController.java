package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.ReviewDto;
import com.shop.ecommerce.mapper.ReviewMapper;
import com.shop.ecommerce.service.ReviewService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @GetMapping("/reviews/product/{productId}")
    public String listReviewsByProduct(@PathVariable Long productId, Model model) {
        List<ReviewDto> reviews = reviewService.findAllByProductId(productId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());

        String productName = reviewService.getProductName(productId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new ReviewDto());
        model.addAttribute("productId", productId);
        model.addAttribute("productName", productName);

        return "reviews";
    }

    @PostMapping("/reviews/product/{productId}/add")
    public String addReview(@PathVariable Long productId,
                            @ModelAttribute("newReview") ReviewDto reviewDto,
                            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        reviewService.addReview(reviewMapper.toEntity(reviewDto), username, productId);

        return "redirect:/reviews/product/" + productId;
    }

    @PostMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable Long id,
                             @ModelAttribute ReviewDto reviewDto,
                             Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        try {
            reviewService.updateReview(id, reviewMapper.toEntity(reviewDto), username, isAdmin);
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        }

        return "redirect:/reviews/product/" + reviewDto.getProductId();
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                               @RequestParam Long productId,
                               Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        try {
            reviewService.deleteReview(id, username, isAdmin);
        } catch (AccessDeniedException e) {
            return "redirect:/access-denied";
        }

        return "redirect:/reviews/product/" + productId;
    }

    @GetMapping("/reviewsEdit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @RequestParam Long productId,
                               Authentication auth,
                               Model model) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        ReviewDto reviewDto = reviewMapper.toDto(
                reviewService.findByIdIfAuthorized(id, username, isAdmin)
        );

        model.addAttribute("review", reviewDto);
        model.addAttribute("productId", productId);
        return "reviewEdit"; // шаблон находится в templates/
    }
}
