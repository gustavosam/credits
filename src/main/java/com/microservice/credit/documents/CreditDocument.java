package com.microservice.credit.documents;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

/**
 * Esta clase representa una colección en la base de mongo.
 * */
@Document(collection = "credits")
@Getter
@Setter
public class CreditDocument {

  @Id
  private String creditNumber;

  private String creditType;

  private Double creditAmount;

  private Double pendingPay;

  private Double amountPaid;

  private String clientDocument;

  private LocalDate creditDate;

}