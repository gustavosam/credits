package com.microservice.credit.util;

import com.microservice.credit.model.Credit;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase extiende de Credit y añadirá atributos que se mostrarán en la consulta
 * del servicio.
 * */
@Getter
@Setter
public class CreditDto extends Credit {

  private String creditNumber;

  private String creditType;

  private Double creditAmount;

  private Double pendingPay;

  private Double amountPaid;

  private String clientDocument;

  private LocalDate creditDate;
}
