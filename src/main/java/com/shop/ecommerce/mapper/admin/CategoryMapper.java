package com.shop.ecommerce.mapper.admin;

import com.shop.ecommerce.dto.admin.CategoryDto;
import com.shop.ecommerce.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }

        // Рекурсивно преобразуем детей
        List<CategoryDto> childrenDtos = category.getChildren()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        dto.setChildren(childrenDtos);

        return dto;
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Category toEntity(CategoryDto dto, Category parentCategory) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setParent(parentCategory);
        return category;
    }

    public void updateEntity(Category category, CategoryDto dto, Category parentCategory) {
        category.setName(dto.getName());
        category.setParent(parentCategory);
    }
}
