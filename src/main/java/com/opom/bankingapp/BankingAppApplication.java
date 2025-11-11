package com.opom.bankingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BankingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAppApplication.class, args);
        System.out.println("Started Banking App at http://localhost:7777");
    }
}
