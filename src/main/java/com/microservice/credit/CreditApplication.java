package com.microservice.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Aplicación principal del microservicio de creditos.
 * */
@SpringBootApplication
@EnableFeignClients
public class CreditApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditApplication.class, args);
  }

}
