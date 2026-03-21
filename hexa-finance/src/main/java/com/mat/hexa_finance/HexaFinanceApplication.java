package com.mat.hexa_finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mat.hexa_finance")
public class HexaFinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HexaFinanceApplication.class, args);
    }
}
