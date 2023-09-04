package com.microservice.credit.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase contendrá los atributos que se enviarán
 * al microservicio de movements.
 * */
@Getter
@Setter
public class Movement {

  private Double amount;

  private String clientDocument;

  private String creditNumber;

  private String movementType;

}
