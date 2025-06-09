package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.dto.admin.ProductDto;
import com.shop.ecommerce.service.admin.CategoryService;
import com.shop.ecommerce.service.admin.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // Список продуктов
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products-list";
    }

    // Форма создания продукта
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-create";
    }

    // Создание продукта
    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") ProductDto productDto, Model model) {
        try {
            productService.createProduct(productDto);
            return "redirect:/admin/products";
        } catch (RuntimeException e) {
            model.addAttribute("creationError", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product-create";
        }
    }

    // Форма редактирования продукта
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductDto productDto = productService.getProductById(id);
        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-edit";
    }

    // Обновление продукта
    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute("product") ProductDto productDto, Model model) {
        try {
            productService.updateProduct(productDto.getId(), productDto);
            return "redirect:/admin/products";
        } catch (RuntimeException e) {
            model.addAttribute("updateError", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product-edit";
        }
    }

    // Удаление продукта
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
