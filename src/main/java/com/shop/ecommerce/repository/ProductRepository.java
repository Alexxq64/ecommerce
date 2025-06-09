package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Поиск продуктов по части имени (без учёта регистра)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Поиск продуктов по id категории
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCategoryIdIn(List<Long> categoryIds);

    // Новый метод для пагинации и фильтрации по названию и категориям
    @Query("SELECT p FROM Product p WHERE " +
            "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:categoryIds IS NULL OR p.category.id IN :categoryIds)")
    Page<Product> findByNameContainingIgnoreCaseAndCategoryIdIn(
            @Param("search") String search,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable);
}
