package ru.sorokin.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class AdminOrdersController {

    @GetMapping("/orders")
    public Mono<String> ordersPage() {
        return Mono.just("orders-admin");
    }
}
