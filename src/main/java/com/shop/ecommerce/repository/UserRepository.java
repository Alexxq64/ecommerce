package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Поиск пользователя по имени
    boolean existsByEmail(String email);  // Проверка существования пользователя по email
    boolean existsByUsername(String username);  // Проверка существования пользователя по имени
}
