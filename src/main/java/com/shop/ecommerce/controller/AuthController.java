package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.AuthDto;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // Передаем весь объект AuthDto в метод сервиса
        userService.registerUser(user);
        return "User registered successfully";
    }
}
