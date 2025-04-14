package ru.sorokin.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mary")
public class Mery {
    @GetMapping
    ResponseEntity<String> test() {
        return ResponseEntity.ok("Привет малышка!!!!!!");
    }

}
