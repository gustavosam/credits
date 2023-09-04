package com.microservice.credit.feignclient;

import com.microservice.credit.util.Movement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Esta clase nos permite conectarnos al microservicio movements.
 * */
@FeignClient(name = "ms-movements", url = "localhost:8084")
public interface MovementFeignClient {

  @PostMapping("/movement/credit")
  void saveMovement(Movement movement);
}
