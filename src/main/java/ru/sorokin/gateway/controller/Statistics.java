package ru.sorokin.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class Statistics {
    @GetMapping
    void test() {
        System.out.println("statistics супер гуд");
    }

}
