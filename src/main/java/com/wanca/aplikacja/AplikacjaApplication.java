package com.wanca.aplikacja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class AplikacjaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AplikacjaApplication.class, args);
    }
}
