package com.shop.ecommerce.repository;

import com.shop.ecommerce.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientId(Long userId);
    List<Message> findBySenderId(Long userId);
}
