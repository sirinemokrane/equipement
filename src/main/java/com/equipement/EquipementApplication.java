package com.equipement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.equipement.controller",
        "com.equipement.services",
        "com.equipement.services",
        "com.equipement.Repository",
        "com.equipement.config"
})
public class EquipementApplication {
    public static void main(String[] args) {
        SpringApplication.run(EquipementApplication.class, args);
    }
}