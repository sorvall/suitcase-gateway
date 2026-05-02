package ru.sorokin.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/statistics")
public class Statistics {
    @GetMapping
    public Mono<Void> test() {
        System.out.println("statistics супер гуд");
        return Mono.empty();
    }

}
