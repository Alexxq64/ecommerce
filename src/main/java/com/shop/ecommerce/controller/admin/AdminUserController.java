package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }
}
