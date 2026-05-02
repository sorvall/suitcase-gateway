package ru.sorokin.gateway.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

/**
 * Раздаёт HTML из classpath:/static/ (собирается Vite в {@code frontend/dist}).
 */
@Controller
public class HtmlDocumentController {

    private static Mono<Resource> html(String name) {
        return Mono.just(new ClassPathResource("static/" + name));
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<Resource> index() {
        return html("index.html");
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<Resource> login() {
        return html("login.html");
    }

    @GetMapping(value = "/orders", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<Resource> orders() {
        return html("orders.html");
    }

    @GetMapping(value = "/dashboard-page", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<Resource> dashboardPage() {
        return html("dashboard-page.html");
    }
}
