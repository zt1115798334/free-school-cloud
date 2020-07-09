package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhang
 */
@EnableFeignClients
@SpringBootApplication
public class ServiceInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceInformationApplication.class, args);
    }

}
