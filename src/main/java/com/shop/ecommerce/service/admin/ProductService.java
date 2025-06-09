package com.shop.ecommerce.service.admin;

import com.shop.ecommerce.dto.admin.ProductDto;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.mapper.admin.ProductMapper;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    // Получить все продукты (используется и в админке, и на фронте)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
        return productMapper.toDto(product);
    }

    public ProductDto createProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + dto.getCategoryId()));

        Product product = productMapper.toEntity(dto, category);

        return productMapper.toDto(productRepository.save(product));
    }

    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + dto.getCategoryId()));

        productMapper.updateEntity(existing, dto, category);

        return productMapper.toDto(productRepository.save(existing));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Фильтрация с пагинацией для публичного каталога
    public Page<ProductDto> getFilteredProducts(String search, List<Long> categoryIds, Pageable pageable) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            categoryIds = null;
        }
        Page<Product> productsPage = productRepository.findByNameContainingIgnoreCaseAndCategoryIdIn(
                search, categoryIds, pageable);
        return productsPage.map(productMapper::toDto);
    }

    public Page<ProductDto> getFilteredProducts(String search, Long categoryId, Pageable pageable) {
        List<Long> categoryIds = categoryId != null ? List.of(categoryId) : null;
        return getFilteredProducts(search, categoryIds, pageable);
    }
}
