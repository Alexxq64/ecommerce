package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.enums.Role;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.dto.AuthDto;  // Импортируем AuthDto
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Интерфейс, бин создаётся в SecurityConfig

    // Метод для регистрации нового пользователя с использованием AuthDto
    public void register(AuthDto authDto) {
        // Шифруем пароль перед сохранением
        String encodedPassword = passwordEncoder.encode(authDto.getPassword());

        // Создаем нового пользователя
        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(authDto.getEmail());
        user.setRole(authDto.getRole() != null ? authDto.getRole() : Role.USER);

        // Сохраняем пользователя в БД
        userRepository.save(user);
    }

    // Метод для входа пользователя
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // Метод для поиска всех пользователей
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Метод для поиска пользователя по ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Метод для поиска пользователя по имени
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод для удаления пользователя по ID
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);  // Проверка перед удалением
        }
        userRepository.deleteById(id);
    }
}
