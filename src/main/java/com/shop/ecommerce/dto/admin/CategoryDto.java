package com.shop.ecommerce.dto.admin;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;      // ID родительской категории (null для корневой)
    private String parentName;  // Название родителя (для отображения, не обязательно)
    private List<CategoryDto> children = new ArrayList<>(); // дочерние категории
}
