package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.AuthDto;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.enums.Role;
import com.shop.ecommerce.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody AuthDto authDto) {
        if (!authDto.getPassword().equals(authDto.getPasswordConfirm())) {
            return "❌ Пароли не совпадают";
        }

        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setEmail(authDto.getEmail());
        user.setRole(Role.USER); // Устанавливаем роль по умолчанию

        try {
            userService.createUser(user, authDto.getPassword());
            return "✅ Пользователь успешно зарегистрирован";
        } catch (RuntimeException e) {
            return "❌ Ошибка: " + e.getMessage();
        }
    }
}
