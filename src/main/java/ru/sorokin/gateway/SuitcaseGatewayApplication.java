package ru.sorokin.gateway;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SuitcaseGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuitcaseGatewayApplication.class, args);
        System.out.println("UP!!!");
    }

}
