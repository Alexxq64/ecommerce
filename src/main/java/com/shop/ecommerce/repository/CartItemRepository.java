package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Получить все позиции корзины конкретного пользователя
    List<CartItem> findByUserId(Long userId);

    // Удалить все позиции корзины пользователя — очистка корзины
    void deleteByUserId(Long userId);

    // Найти позицию корзины по пользователю и продукту (для добавления товара)
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
}
