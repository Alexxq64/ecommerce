package com.shop.ecommerce.config;

import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/panel", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider() {

            @Override
            protected void additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails userDetails,
                                                          org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication) {
                String presentedPassword = authentication.getCredentials().toString();
                String dbPassword = userDetails.getPassword();

                boolean passwordMatches;

                // Если пароль в базе короче 30 символов — считаем, что это plain-text
                if (dbPassword.length() < 30) {
                    // Сравниваем напрямую
                    passwordMatches = presentedPassword.equals(dbPassword);
                    if (passwordMatches) {
                        // Хешируем и обновляем пароль в базе
                        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
                            user.setPassword(passwordEncoder().encode(presentedPassword));
                            userRepository.save(user);
                        });
                    }
                } else {
                    // Иначе сравниваем с помощью passwordEncoder
                    passwordMatches = passwordEncoder().matches(presentedPassword, dbPassword);
                }

                if (!passwordMatches) {
                    throw new BadCredentialsException("Bad credentials");
                }
            }
        };

        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ApplicationRunner passwordCheckRunner() {
//        return args -> {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            String rawPassword = "qq";
//            String dbPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq7MZYVSqJz4tJwS2QO3dQ9XHjDqG2";
//            String newHash = encoder.encode(rawPassword);
//
//            System.out.println("\n=== Security Configuration Check ===");
//            System.out.println("Password matches: " + encoder.matches(rawPassword, dbPassword));
//            System.out.println("New hash for verification: " + newHash);
//            System.out.println("New hash matches raw: " + encoder.matches(rawPassword, newHash));
//            System.out.println("====================================\n");
//        };
//    }
//
//    @Bean
//    public ApplicationRunner passwordHashGenerator() {
//        return args -> {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            String rawPassword = "qq";
//            String encodedPassword = encoder.encode(rawPassword);
//            System.out.println("BCrypt hash for 'qq': " + encodedPassword);
//        };
//    }
}
