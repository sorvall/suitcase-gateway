/*
package ru.sorokin.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()  // Разрешаем все запросы
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Отключаем CSRF
                // Перенаправление HTTP -> HTTPS (альтернатива requiresChannel из MVC)
                .redirectToHttps(redirect -> redirect
                        .httpsRedirectWhen(e -> true)  // Всегда редиректим на HTTPS
                )
                .build();
    }
}*/
