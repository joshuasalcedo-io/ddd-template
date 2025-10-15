package com.example.ddd.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application entry point.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.example.ddd")
@EntityScan(basePackages = "com.example.ddd.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.example.ddd.infrastructure.persistence")
public class DddApplication {

    public static void main(String[] args) {
        SpringApplication.run(DddApplication.class, args);
    }
}
