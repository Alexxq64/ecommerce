package com.shop.ecommerce.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/panel")
    public String adminPanel(Model model) {
        return "admin/admin-panel"; // путь до шаблона: templates/admin/admin-panel.html
    }

}
