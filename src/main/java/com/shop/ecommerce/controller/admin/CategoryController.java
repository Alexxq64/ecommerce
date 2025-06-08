package com.shop.ecommerce.controller.admin;

import com.shop.ecommerce.dto.admin.CategoryDto;
import com.shop.ecommerce.service.admin.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories-list"; // 🛠 ИСПРАВЛЕНО
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "admin/category-create"; // 🛠 ИСПРАВЛЕНО
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") CategoryDto categoryDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(categoryDto);
            redirectAttributes.addFlashAttribute("successMessage", "Категория успешно создана");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании категории: " + e.getMessage());
            return "redirect:/admin/categories/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CategoryDto categoryDto = categoryService.getCategoryById(id);
            model.addAttribute("category", categoryDto);
            model.addAttribute("allCategories", categoryService.getAllCategories());
            return "admin/category-edit"; // 🛠 ИСПРАВЛЕНО
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Категория не найдена");
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute("category") CategoryDto categoryDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, categoryDto);
            redirectAttributes.addFlashAttribute("successMessage", "Категория успешно обновлена");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении категории: " + e.getMessage());
            return "redirect:/admin/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Категория успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении категории: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}
