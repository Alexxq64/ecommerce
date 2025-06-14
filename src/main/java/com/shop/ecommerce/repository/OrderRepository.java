package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByUserUsername(String username);
}
