package com.microservice.credit.service.mapper;

import com.microservice.credit.util.MovementDto;

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
  public static MovementDto setValues(Double amount, String clientDocument,
                                      String creditNumber, String movementType) {

    MovementDto movementDto = new MovementDto();
    movementDto.setAmount(amount);
    movementDto.setClientDocument(clientDocument);
    movementDto.setCreditNumber(creditNumber);
    movementDto.setMovementType(movementType);

    return movementDto;
  }
}
