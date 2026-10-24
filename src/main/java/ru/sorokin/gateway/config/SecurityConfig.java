package ru.sorokin.gateway.config;

import java.net.URI;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint.DelegateEntry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String ADMIN_USER = "admin";

    /** API заказов и графика, проксируемое в suitcase-booking; без входа отвечает 401, а не редиректом. */
    private static final String[] ADMIN_API = {"/booking", "/booking/**", "/dashboard", "/dashboard/**"};

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login", "/error").permitAll()
                        .pathMatchers("/", "/css/**", "/images/**", "/js/**", "/static/**").permitAll()
                        .pathMatchers("/favicon.ico", "/robots.txt", "/sitemap.xml").permitAll()
                        .pathMatchers("/yandex_*").permitAll()
                        // Публичная форма на сайте: проверка доступности и создание брони
                        .pathMatchers(HttpMethod.GET, "/availability", "/availability/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/booking").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        // Админка: страницы и API со списком заказов, подтверждением/отменой и графиком
                        .pathMatchers("/admin", "/orders", "/dashboard-page").authenticated()
                        .pathMatchers(ADMIN_API).authenticated()
                        .anyExchange().permitAll()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        // После входа — на страницу, куда шли, иначе в админку
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/admin")))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))
                .logout(logout -> {
                    RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
                    successHandler.setLogoutSuccessUrl(URI.create("/"));
                    logout.logoutSuccessHandler(successHandler);
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    private static ServerAuthenticationEntryPoint authenticationEntryPoint() {
        DelegateEntry api = new DelegateEntry(
                ServerWebExchangeMatchers.pathMatchers(ADMIN_API),
                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
        DelegatingServerAuthenticationEntryPoint entryPoint = new DelegatingServerAuthenticationEntryPoint(api);
        entryPoint.setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint("/login"));
        return entryPoint;
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
