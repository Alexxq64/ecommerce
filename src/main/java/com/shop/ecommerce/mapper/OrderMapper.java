package com.shop.ecommerce.mapper;

import com.shop.ecommerce.dto.OrderDto;
import com.shop.ecommerce.dto.OrderItemDto;
import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus());
        dto.setItems(order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList()));

        // Вычисляем сумму заказа
        double total = order.getOrderItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        dto.setTotalAmount(total);

        return dto;
    }

    public OrderItemDto toOrderItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
