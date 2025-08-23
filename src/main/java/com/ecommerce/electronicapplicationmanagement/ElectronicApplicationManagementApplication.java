package com.ecommerce.electronicapplicationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ElectronicApplicationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicApplicationManagementApplication.class, args);
    }

}
