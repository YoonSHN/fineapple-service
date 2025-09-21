package com.fineapple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FineappleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FineappleApplication.class, args);

    }
}
