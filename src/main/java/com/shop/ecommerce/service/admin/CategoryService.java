package com.shop.ecommerce.service.admin;

import com.shop.ecommerce.dto.admin.CategoryDto;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.mapper.admin.CategoryMapper;
import com.shop.ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public List<CategoryDto> getRootCategories() {
        return categoryRepository.findByParentIsNull()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public List<CategoryDto> getChildren(Long parentId) {
        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
        return categoryMapper.toDto(category);
    }

    public CategoryDto createCategory(CategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Категория с таким именем уже существует");
        }

        Category parent = null;
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
        }

        Category category = categoryMapper.toEntity(dto, parent);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (dto.getParentId() != null && dto.getParentId().equals(id)) {
            throw new IllegalArgumentException("Категория не может быть родителем самой себя");
        }

        Category parent = null;
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
        }

        categoryMapper.updateEntity(existing, dto, parent);

        return categoryMapper.toDto(categoryRepository.save(existing));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        List<Category> children = categoryRepository.findByParentId(id);
        for (Category child : children) {
            child.setParent(null);
            categoryRepository.save(child);
        }

        categoryRepository.delete(category);
    }
}
