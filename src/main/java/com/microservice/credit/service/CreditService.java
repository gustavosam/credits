package com.microservice.credit.service;

import com.microservice.credit.model.Credit;
import com.microservice.credit.util.ClientDto;
import com.microservice.credit.util.CreditDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Esta interfaz contiene los métodos que implementará CreditServiceImpl.
 * */
public interface CreditService {

  Mono<CreditDto> createCredit(Double amount, ClientDto clientDto);

  Mono<ClientDto> getClient(String clientDocument);

  Mono<Boolean> personalCreditExist(ClientDto calledCustomer);

  Flux<Credit> getCredits(String clientDocument);

  Mono<CreditDto> payCredit(String creditNumber, Double amount);

  Mono<Boolean> validateOverPayment(String creditNumber, Double amount);

  Mono<Boolean> creditExist(String creditNumber);
}
