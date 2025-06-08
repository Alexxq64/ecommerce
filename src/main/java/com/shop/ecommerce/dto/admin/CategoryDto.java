package com.shop.ecommerce.dto.admin;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;      // ID родительской категории (null для корневой)
    private String parentName;  // Название родителя (для отображения, не обязательно)
}
