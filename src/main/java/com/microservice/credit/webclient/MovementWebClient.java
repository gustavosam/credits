package com.microservice.credit.webclient;

import com.microservice.credit.util.MovementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Clase para comunicación de microservicios.
 * */
@Component
public class MovementWebClient {

  @Autowired
  private WebClient.Builder webClientBuilder;

  /**
   * Se comunica con el microservicios de movimientos.
   * */
  public Mono<Void> saveMovement(MovementDto movementDto) {
    return webClientBuilder.build()
            .post()
            .uri("http://localhost:8084/movement")
            .body(BodyInserters.fromValue(movementDto))
            .retrieve()
            .bodyToMono(Void.class);
  }
}
