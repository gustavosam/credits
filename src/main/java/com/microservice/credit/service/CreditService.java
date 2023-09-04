package com.microservice.credit.service;

import com.microservice.credit.model.Credit;
import com.microservice.credit.util.Client;
import com.microservice.credit.util.CreditDto;
import java.util.List;

/**
 * Esta interfaz contiene los métodos que implementará CreditServiceImpl.
 * */
public interface CreditService {

  CreditDto createCredit(Double amount, Client client);

  Client getClient(String clientDocument);

  Boolean personalCreditExist(Client calledCustomer);

  List<Credit> getCredits(String clientDocument);

  CreditDto payCredit(String creditNumber, Double amount);

  Boolean validateOverPayment(String creditNumber, Double amount);

  Boolean creditExist(String creditNumber);
}
