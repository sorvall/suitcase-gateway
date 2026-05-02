package ru.sorokin.gateway.config;

import java.net.URI;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String ADMIN_USER = "admin";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login", "/error").permitAll()
                        .pathMatchers("/", "/css/**", "/images/**", "/js/**", "/static/**").permitAll()
                        .pathMatchers("/favicon.ico", "/robots.txt", "/sitemap.xml").permitAll()
                        .pathMatchers("/yandex_*").permitAll()
                        .pathMatchers("/booking/**", "/availability/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/statistics/**").permitAll()
                        .pathMatchers("/orders", "/dashboard-page").authenticated()
                        .pathMatchers("/dashboard", "/dashboard/**").permitAll()
                        .anyExchange().permitAll()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(form -> form.loginPage("/login"))
                .logout(logout -> {
                    RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
                    successHandler.setLogoutSuccessUrl(URI.create("/"));
                    logout.logoutSuccessHandler(successHandler);
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username(ADMIN_USER)
                .password(encoder.encode("5564"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(List.of(admin));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
