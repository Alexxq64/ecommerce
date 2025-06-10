package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.CartItemDto;
import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.OrderItem;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.enums.OrderStatus;
import com.shop.ecommerce.repository.OrderRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    // Поиск и сортировка в сервисе
    public List<Order> getByUser(User user) {
        return orderRepository.findByUser(user).stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();
    }

    public List<Order> getByUsername(String username) {
        return orderRepository.findByUserUsername(username).stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();
    }

    @Transactional
    public Order createOrderFromCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        List<CartItemDto> cartItems = cartItemService.getUserCart(username);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Корзина пуста. Оформление заказа невозможно.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("НОВЫЙ");

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto dto : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Продукт не найден с id = " + dto.getProductId()));

            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getProductPrice());
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartItemService.clearCart(username);

        return savedOrder;
    }

    public OrderStatus getOrderStatus(Order order) {
        try {
            return OrderStatus.valueOf(order.getStatus());
        } catch (IllegalArgumentException | NullPointerException e) {
            // Если статус в базе неожиданно невалидный — возвращаем НОВЫЙ по умолчанию
            return OrderStatus.НОВЫЙ;
        }
    }

    public void setOrderStatus(Order order, OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("OrderStatus не может быть null");
        }
        order.setStatus(status.name());
    }

}

