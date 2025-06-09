package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.admin.ProductDto;
import com.shop.ecommerce.dto.admin.CategoryDto;
import com.shop.ecommerce.service.admin.ProductService;
import com.shop.ecommerce.service.admin.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String viewCatalog(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        int pageSize = 12; // можно вынести в конфиг

        Page<ProductDto> products = productService.getFilteredProducts(search, categoryId, PageRequest.of(page, pageSize));
        List<CategoryDto> categories = categoryService.getCategoryTree();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);

        return "index";
    }
}
