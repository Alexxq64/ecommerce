package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Получить корневые категории (у которых parent == null)
    List<Category> findByParentIsNull();

    // Получить дочерние категории по parent id
    List<Category> findByParentId(Long parentId);

    // Поиск категории по имени
    Optional<Category> findByName(String name);

    // Проверка существования категории по имени
    boolean existsByName(String name);

    // Получить все категории, у которых parent есть (не корневые)
    List<Category> findByParentIsNotNull();

    // Получить все категории с конкретным родителем (по объекту)
    List<Category> findByParent(Category parent);

    // Получить количество дочерних категорий у родителя
    int countByParent(Category parent);
}
