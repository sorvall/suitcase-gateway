package ru.sorokin.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class Dashboard {
    @GetMapping("/dashboard-page")
    public Mono<String> dashboardPage() {
        return Mono.just("gantt-chart");
    }
}
