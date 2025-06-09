package com.shop.ecommerce.mapper;

import com.shop.ecommerce.dto.CartItemDto;
import com.shop.ecommerce.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDto toDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setProductPrice(cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        return dto;
    }

    // Для создания/обновления сущности из DTO и пользователя
    public CartItem toEntity(CartItemDto dto, com.shop.ecommerce.entity.User user, com.shop.ecommerce.entity.Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setId(dto.getId());
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }
}
