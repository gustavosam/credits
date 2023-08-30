package com.microservice.credit.customer;

import com.microservice.credit.documents.Customers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-customers", url ="localhost:8080")
public interface CustomerFeignClient {

    @GetMapping("/customer/{customerDocument}")
    Customers getCustomerById(@PathVariable String customerDocument);
}
