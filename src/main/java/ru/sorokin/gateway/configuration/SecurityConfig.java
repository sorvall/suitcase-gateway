/*
package ru.sorokin.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/test", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Client(withDefaults())  // Настройка OAuth2 Client (замена oauth2Login)
                .formLogin(form -> form
                        .defaultSuccessUrl("/test", true)  // Перенаправление после успешного входа
                        .failureUrl("/login?error=true")         // Страница при ошибке входа
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")  // (Опционально) Обработка 403
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Рекомендуется использовать BCrypt
    }
}*/
