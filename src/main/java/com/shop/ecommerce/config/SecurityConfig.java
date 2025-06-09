package com.shop.ecommerce.config;

import com.shop.ecommerce.security.CustomAuthenticationSuccessHandler;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/admin/products/view/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)          // Используем кастомный success handler
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
                    passwordMatches = presentedPassword.equals(dbPassword);
                    if (passwordMatches) {
                        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
                            user.setPassword(passwordEncoder().encode(presentedPassword));
                            userRepository.save(user);
                        });
                    }
                } else {
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
}
