package com.microservice.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Aplicación principal del microservicio de creditos.
 * */
@EnableDiscoveryClient
@SpringBootApplication
public class CreditApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditApplication.class, args);
  }

}
