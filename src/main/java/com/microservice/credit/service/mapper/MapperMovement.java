package com.microservice.credit.service.mapper;

import com.microservice.credit.util.Movement;

/**
 * Esta clase contiene un método que genera un objeto MovementsDocuments
 * Para mandar esa información al microservicio de movements
 * y posteriormente guardar el movimiento en mongo db.
 * */
public class MapperMovement {

  /**
   * Esta método recibe como parámetro información para guardar movimientos
   * Se obtienen todos los valores y se asignan a un objeto MovementsDocuments.
   * */
  public static Movement setValues(Double amount, String clientDocument,
                                   String creditNumber, String movementType) {

    Movement movement = new Movement();
    movement.setAmount(amount);
    movement.setClientDocument(clientDocument);
    movement.setCreditNumber(creditNumber);
    movement.setMovementType(movementType);

    return movement;
  }
}
