package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.CartItemDto;
import com.shop.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        cartItemService.addToCart(productId, userDetails.getUsername());
        return "redirect:/"; // Возврат на главную страницу
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        cartItemService.removeFromCart(cartItemId, userDetails.getUsername());
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartItemService.clearCart(userDetails.getUsername());
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        List<CartItemDto> items = cartItemService.getUserCart(userDetails.getUsername());
        model.addAttribute("cartItems", items);
        return "cart";
    }
}
