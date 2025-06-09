package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.OrderDto;
import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.service.CartItemService;
import com.shop.ecommerce.service.OrderService;
import com.shop.ecommerce.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/orders/create")
    public String createOrder(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        orderService.createOrderFromCart(username);
        cartItemService.clearCart(username);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        List<Order> orders = orderService.getByUsername(username);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .toList();
        model.addAttribute("orders", orderDtos);
        return "history";
    }
}
