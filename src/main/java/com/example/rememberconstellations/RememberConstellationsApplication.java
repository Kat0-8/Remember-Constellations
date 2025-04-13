package com.example.rememberconstellations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RememberConstellationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RememberConstellationsApplication.class, args);
    }
}
