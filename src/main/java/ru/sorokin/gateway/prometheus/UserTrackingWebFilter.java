package ru.sorokin.gateway.prometheus;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UserTrackingWebFilter implements WebFilter {
    private final UserMetrics userMetrics;

    public UserTrackingWebFilter(UserMetrics userMetrics) {
        this.userMetrics = userMetrics;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Проверяем, есть ли сессия (в реактивном стиле)
        return exchange.getSession()
                .doOnNext(session -> {
                    if (session != null) {
                        userMetrics.incrementUserCount();
                    }
                })
                .then(chain.filter(exchange));
    }
}