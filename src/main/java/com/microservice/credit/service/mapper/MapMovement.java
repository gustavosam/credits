package com.microservice.credit.service.mapper;

import com.microservice.credit.util.MovementDto;
import org.mapstruct.Mapper;

/**
 * Interfaz de mapstruct para mapear clases.
 * */
@Mapper(componentModel = "spring")
public interface MapMovement {

  MovementDto setValues(Double amount, String clientDocument,
                        String creditNumber, String movementType);
}
