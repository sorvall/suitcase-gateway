package ru.sorokin.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN_USER = "admin";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/error").permitAll()
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/static/**").permitAll()
                        .requestMatchers("/favicon.ico", "/robots.txt", "/sitemap.xml").permitAll()
                        .requestMatchers("/yandex_*").permitAll()
                        .requestMatchers("/booking/**", "/availability/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/statistics/**").permitAll()
                        .requestMatchers("/orders", "/dashboard-page").authenticated()
                        .requestMatchers("/dashboard", "/dashboard/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/booking/**", "/availability/**", "/dashboard/**")
                );
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username(ADMIN_USER)
                .password(encoder.encode("5564"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
