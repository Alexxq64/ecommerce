package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-create";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam String password,
                             @RequestParam String passwordConfirm,
                             Model model) {
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "admin/user-create";
        }
        try {
            userService.createUser(user, password);
            return "redirect:/admin/users";
        } catch (RuntimeException e) {
            model.addAttribute("creationError", e.getMessage());
            return "admin/user-create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
