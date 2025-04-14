package ru.sorokin.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    ResponseEntity<String> test(@PathVariable("text") String text) {
        return ResponseEntity.ok(text);
    }

}
