package com.mat.hexa_finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.mat.hexa_finance")
@EnableScheduling
public class HexaFinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HexaFinanceApplication.class, args);
    }
}
