package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/admin/users/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-create";
    }

    @PostMapping("/admin/users/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("password") String password,
                             @RequestParam("passwordConfirm") String passwordConfirm,
                             Model model) {
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "admin/user-create";
        }
        try {
            userService.createUser(user, password);
        } catch (RuntimeException e) {
            model.addAttribute("user", user);
            model.addAttribute("creationError", e.getMessage());
            return "admin/user-create";
        }
        return "redirect:/admin/users";
    }

}
