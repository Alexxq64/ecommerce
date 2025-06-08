package com.shop.ecommerce.dto.admin;

import com.shop.ecommerce.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private LocalDateTime createdAt;
}
