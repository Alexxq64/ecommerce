package com.shop.ecommerce.dto;

import com.shop.ecommerce.enums.Role;
import lombok.Data;  // Для генерации геттеров, сеттеров, toString, equals и hashCode
import lombok.NoArgsConstructor;  // Для генерации конструктора без параметров
import lombok.AllArgsConstructor;  // Для генерации конструктора с параметрами

@Data  // Генерирует геттеры, сеттеры, toString, equals, hashCode и другие
@NoArgsConstructor  // Генерирует конструктор без параметров
@AllArgsConstructor  // Генерирует конструктор с параметрами
public class AuthDto {

    private String username;
    private String password;
    private String email;
    private Role role;  // Добавлено

}
