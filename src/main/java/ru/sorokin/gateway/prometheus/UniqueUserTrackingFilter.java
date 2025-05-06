package ru.sorokin.gateway.prometheus;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UniqueUserTrackingFilter implements WebFilter {
    private final UniqueUserMetrics userMetrics;

    public UniqueUserTrackingFilter(UniqueUserMetrics userMetrics) {
        this.userMetrics = userMetrics;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = extractUserId(exchange);
        if (userId != null) {
            userMetrics.trackUser(userId);
        }
        return chain.filter(exchange);
    }

    private String extractUserId(ServerWebExchange exchange) {
        // 1. Пробуем получить ID из сессии
        return exchange.getSession()
                .map(session -> {
                    if (session.getAttributes().isEmpty()) {
                        return "session_" + session.getId();
                    }
                    return null;
                })
                .defaultIfEmpty(
                        // 2. Если нет сессии - используем IP + User-Agent
                        "ip_" + exchange.getRequest().getRemoteAddress() +
                                "_ua_" + exchange.getRequest().getHeaders().getFirst("User-Agent")
                )
                .block(); // Для простоты (в реальном проекте лучше избегать block())
    }
}
