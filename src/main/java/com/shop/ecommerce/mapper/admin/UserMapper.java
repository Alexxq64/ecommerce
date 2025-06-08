package com.shop.ecommerce.mapper.admin;

import com.shop.ecommerce.dto.admin.UserDto;
import com.shop.ecommerce.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId()); // осторожно, id обычно не меняется у новых
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        // пароль нужно устанавливать отдельно, здесь не передаем
        // createdAt обычно устанавливается автоматически
        return user;
    }
}
