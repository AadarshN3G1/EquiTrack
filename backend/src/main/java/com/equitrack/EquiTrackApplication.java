package com.equitrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EquiTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquiTrackApplication.class, args);
    }
}
