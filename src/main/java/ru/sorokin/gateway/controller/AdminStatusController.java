package ru.sorokin.gateway.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Сводка состояния сервисов для админки: health gateway, booking и notification
 * (через их /actuator/health внутри docker-сети) и проверка редиректа http → https.
 */
@RestController
public class AdminStatusController {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);
    private static final ParameterizedTypeReference<Map<String, Object>> JSON_MAP =
            new ParameterizedTypeReference<>() {
            };

    public record Component(String name, String status, String detail) {
    }

    public record ServiceStatus(String id, String name, String status, Long latencyMs, String error,
                                List<Component> components) {
    }

    public record StatusReport(Instant checkedAt, List<ServiceStatus> services) {
    }

    private final WebClient webClient;
    private final HealthEndpoint healthEndpoint;
    private final String bookingUrl;
    private final String notificationUrl;
    private final String redirectUrl;

    public AdminStatusController(WebClient.Builder webClientBuilder,
                                 HealthEndpoint healthEndpoint,
                                 @Value("${admin.status.booking-url:http://suitcase-booking-container:8081}") String bookingUrl,
                                 @Value("${admin.status.notification-url:http://suitcase-notification-container:8082}") String notificationUrl,
                                 @Value("${admin.status.redirect-url:http://bikeboxgo.ru/}") String redirectUrl) {
        this.webClient = webClientBuilder.build();
        this.healthEndpoint = healthEndpoint;
        this.bookingUrl = bookingUrl;
        this.notificationUrl = notificationUrl;
        this.redirectUrl = redirectUrl;
    }

    @GetMapping("/admin/api/status")
    public Mono<StatusReport> status() {
        return Mono.zip(gateway(),
                        actuator("booking", "Booking", bookingUrl),
                        actuator("notification", "Notification", notificationUrl),
                        redirect())
                .map(t -> new StatusReport(Instant.now(), List.of(t.getT1(), t.getT2(), t.getT3(), t.getT4())));
    }

    private Mono<ServiceStatus> gateway() {
        long start = System.nanoTime();
        return Mono.fromCallable(healthEndpoint::health)
                .subscribeOn(Schedulers.boundedElastic())
                .map(health -> new ServiceStatus("gateway", "Gateway", health.getStatus().getCode(),
                        elapsedMs(start), null, components(health)));
    }

    private Mono<ServiceStatus> actuator(String id, String name, String baseUrl) {
        long start = System.nanoTime();
        // При DOWN actuator отвечает 503 с тем же JSON, поэтому читаем тело при любом коде
        return webClient.get()
                .uri(baseUrl + "/actuator/health")
                .exchangeToMono(response -> response.bodyToMono(JSON_MAP).defaultIfEmpty(Map.of()))
                .timeout(TIMEOUT)
                .map(body -> new ServiceStatus(id, name, String.valueOf(body.getOrDefault("status", "UNKNOWN")),
                        elapsedMs(start), null, components(body)))
                .onErrorResume(e -> Mono.just(unreachable(id, name, start, e)));
    }

    private Mono<ServiceStatus> redirect() {
        long start = System.nanoTime();
        // WebClient не следует редиректам сам — ждём 3xx на https
        return webClient.get()
                .uri(redirectUrl)
                .exchangeToMono(response -> Mono.just(new ServiceStatus("redirect", "Редирект http → https",
                        response.statusCode().is3xxRedirection() ? "UP" : "DOWN", elapsedMs(start), null,
                        List.of(new Component("http", response.statusCode().is3xxRedirection() ? "UP" : "DOWN",
                                response.statusCode().value() + " → "
                                        + response.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION))))))
                .timeout(TIMEOUT)
                .onErrorResume(e -> Mono.just(unreachable("redirect", "Редирект http → https", start, e)));
    }

    private static List<Component> components(HealthComponent health) {
        List<Component> result = new ArrayList<>();
        if (health instanceof CompositeHealth composite) {
            composite.getComponents().forEach((name, component) -> result.add(new Component(name,
                    component.getStatus().getCode(),
                    component instanceof Health h ? detail(name, h.getDetails()) : null)));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static List<Component> components(Map<String, Object> body) {
        List<Component> result = new ArrayList<>();
        if (body.get("components") instanceof Map<?, ?> components) {
            ((Map<String, Object>) components).forEach((name, value) -> {
                Map<String, Object> component = value instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of();
                Map<String, Object> details = component.get("details") instanceof Map<?, ?> d
                        ? (Map<String, Object>) d : Map.of();
                result.add(new Component(name, String.valueOf(component.getOrDefault("status", "UNKNOWN")),
                        detail(name, details)));
            });
        }
        return result;
    }

    /** Короткая подпись к компоненту: место на диске, тип БД, версия RabbitMQ, SMTP-сервер. */
    private static String detail(String name, Map<String, Object> details) {
        return switch (name) {
            case "diskSpace" -> details.get("free") instanceof Number free && details.get("total") instanceof Number total
                    ? String.format("свободно %.1f из %.1f ГБ", free.doubleValue() / 1e9, total.doubleValue() / 1e9)
                    : null;
            case "db" -> details.get("database") != null ? String.valueOf(details.get("database")) : null;
            case "rabbit" -> details.get("version") != null ? "v" + details.get("version") : null;
            case "mail" -> details.get("location") != null ? String.valueOf(details.get("location")) : null;
            default -> details.get("error") != null ? String.valueOf(details.get("error")) : null;
        };
    }

    private static ServiceStatus unreachable(String id, String name, long start, Throwable e) {
        String error = e instanceof java.util.concurrent.TimeoutException
                ? "нет ответа за " + TIMEOUT.toSeconds() + " с"
                : e.getClass().getSimpleName() + (e.getMessage() != null ? ": " + e.getMessage() : "");
        return new ServiceStatus(id, name, "UNREACHABLE", elapsedMs(start), error, List.of());
    }

    private static long elapsedMs(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
