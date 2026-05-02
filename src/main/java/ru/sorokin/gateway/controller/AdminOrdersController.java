package ru.sorokin.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminOrdersController {

    @GetMapping("/orders")
    public String ordersPage() {
        return "orders-admin";
    }
}
