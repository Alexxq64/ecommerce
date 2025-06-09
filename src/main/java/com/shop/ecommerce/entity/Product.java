package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(optional = false) // 🔒 обязательно указывать категорию
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private Integer stock; // <-- добавьте это поле
}
