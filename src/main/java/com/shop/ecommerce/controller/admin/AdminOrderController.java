package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.enums.OrderStatus;
import com.shop.ecommerce.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Показать список заказов
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAll();  // метод возвращает все заказы
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values()); // для выпадающего списка
        return "admin/orders-list"; // имя Thymeleaf-шаблона
    }

    // 2. Обновить статус заказа
    @PostMapping("/update-status")
    public String updateStatus(
            @RequestParam("orderId") Long orderId,
            @RequestParam("status") OrderStatus status
    ) {
        Optional<Order> optionalOrder = orderService.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderService.setOrderStatus(order, status);
            orderService.save(order);
        }
        return "redirect:/admin/orders";
    }
}
