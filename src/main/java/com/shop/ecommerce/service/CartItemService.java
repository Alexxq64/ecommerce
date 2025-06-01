package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.CartItem;
import com.shop.ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    public List<CartItem> findByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void deleteByUserId(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
