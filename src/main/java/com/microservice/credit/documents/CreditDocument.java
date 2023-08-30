package com.microservice.credit.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "credits")
@Getter
@Setter
public class CreditDocument {

    @Id
    private String creditNumber;

    private String creditType;

    private Double creditAmount;

    private Double creditPendingPayment;

    private Double creditPaidAmount;

    private String customerDocument;

    private LocalDate creditDate;

}