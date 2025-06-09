package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.ReviewDto;
import com.shop.ecommerce.entity.Review;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @GetMapping
    public String listReviews(Model model) {
        List<ReviewDto> reviews = reviewService.findAll().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new ReviewDto());
        return "reviews";  // создадим этот шаблон
    }

    @GetMapping("/product/{productId}")
    public String listReviewsByProduct(@PathVariable Long productId, Model model) {
        List<ReviewDto> reviews = reviewService.findAllByProductId(productId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new ReviewDto());
        model.addAttribute("productId", productId);
        return "reviews";
    }

//
//    @PostMapping("/add")
//    public String addReview(@ModelAttribute("newReview") ReviewDto reviewDto, Authentication auth) {
//        if (auth == null || !auth.isAuthenticated()) {
//            return "redirect:/login";
//        }
//        String username = auth.getName();
//        reviewService.addReview(reviewMapper.toEntity(reviewDto), username, reviewDto.getProductId());
//        return "redirect:/reviews";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String editReview(@PathVariable Long id, @ModelAttribute ReviewDto reviewDto, Authentication auth) {
//        if (auth == null || !auth.isAuthenticated()) {
//            return "redirect:/login";
//        }
//        String username = auth.getName();
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//        try {
//            reviewService.updateReview(id, reviewMapper.toEntity(reviewDto), username, isAdmin);
//        } catch (AccessDeniedException e) {
//            return "redirect:/access-denied"; // или другую страницу ошибки
//        }
//        return "redirect:/reviews";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteReview(@PathVariable Long id, Authentication auth) {
//        if (auth == null || !auth.isAuthenticated()) {
//            return "redirect:/login";
//        }
//        String username = auth.getName();
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//        try {
//            reviewService.deleteReview(id, username, isAdmin);
//        } catch (AccessDeniedException e) {
//            return "redirect:/access-denied";
//        }
//        return "redirect:/reviews";
//    }
}
