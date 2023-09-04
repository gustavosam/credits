package com.microservice.credit.feignclient;

import com.microservice.credit.util.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Esta interfaz permite conectarnos con el microservicio client
 * para obtener información y verificar si el cliente existe.
 * */
@FeignClient(name = "ms-customers", url = "localhost:8080")
public interface ClientFeignClient {

  @GetMapping("/client/{document}")
   Client getClient(@PathVariable String document);
}
