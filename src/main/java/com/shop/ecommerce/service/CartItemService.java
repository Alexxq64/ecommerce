package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.CartItemDto;
import com.shop.ecommerce.entity.CartItem;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.mapper.CartItemMapper;
import com.shop.ecommerce.repository.CartItemRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    public void addToCart(Long productId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        CartItem existing = cartItemRepository
                .findByUserId(user.getId()).stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            cartItemRepository.save(existing);
        } else {
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(1);
            cartItemRepository.save(item);
        }
    }

    public void removeFromCart(Long cartItemId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();

        if (item.getUser().getId().equals(user.getId())) {
            cartItemRepository.delete(item);
        }
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        cartItemRepository.deleteByUserId(user.getId());
    }

    public List<CartItemDto> getUserCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(cartItemMapper::toDto)
                .toList();
    }
}
