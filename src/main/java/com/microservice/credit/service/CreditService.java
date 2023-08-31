package com.microservice.credit.service;

import com.microservice.credit.documents.Customers;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditRequestPaid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CreditService {

    Credit createCredit(Double amount, Customers customers);

    Customers clientExist(String customerDocument);

    Boolean validateGenerateCredit(Customers calledCustomer);

    List<Credit> getCredits(String customerDocument);

    Credit payCredit(String creditNumber, Double creditPayAmount);

    Boolean validatePayCredit(String creditNumber, Double creditPayAmount);

    Boolean creditExist(String creditNumber);
}
