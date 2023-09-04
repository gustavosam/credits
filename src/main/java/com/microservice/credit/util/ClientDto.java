package com.microservice.credit.util;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase contendrá los atributos que serán llamados
 * desde el microservicio de cliente.
 * */
@Getter
@Setter
public class ClientDto {

  private String document;

  private String name;

  private String clientType;

  private String email;

  private Boolean isActive;

  private LocalDate clientCreationDate;
}
