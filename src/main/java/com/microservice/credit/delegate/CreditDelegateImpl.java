package com.microservice.credit.delegate;

import com.microservice.credit.api.CreditApiDelegate;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditPay;
import com.microservice.credit.model.CreditRequest;
import com.microservice.credit.service.CreditService;
import com.microservice.credit.util.ClientDto;
import com.microservice.credit.util.Constants;
import com.microservice.credit.util.ErrorC;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Esta clase implementa los métodos generados por open api.
 * */

@Service
public class CreditDelegateImpl implements CreditApiDelegate {

  @Autowired
  private CreditService creditService;

  @Override
  public Mono<ResponseEntity<Credit>> createCredit(Mono<CreditRequest> creditRequest,
                                                   ServerWebExchange exchange) {

    return creditRequest.flatMap(credit -> {

      if(credit.getClientDocument() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY)));
      }

      if(credit.getAmount() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CREDIT_EMPTY)));
      }

      ClientDto clientDto = creditService.getClient(credit.getClientDocument());

      if(clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_COMPANY)
              || clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_PYME)
              || clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_VIP)){

        return creditService.createCredit(credit.getAmount(), clientDto)
                .map(creditDto -> ResponseEntity.status(HttpStatus.CREATED).body(creditDto));
      }

      return creditService.personalCreditExist(clientDto).flatMap(value -> {

        if(value){
          return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CLIENT_HAS_CREDIT)));
        }

        return creditService.createCredit(credit.getAmount(), clientDto)
                .map(creditDto -> ResponseEntity.status(HttpStatus.CREATED).body(creditDto));

      });

    });

    /*if (credit.getClientDocument() == null || credit.getClientDocument().isEmpty()) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY));
    }

    if (credit.getAmount() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CREDIT_EMPTY));
    }*/

    /*ClientDto clientDto = creditService.getClient(credit.getClientDocument());

    if (clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_COMPANY)
            || clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_PYME)
            || clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_VIP)) {

      return ResponseEntity.status(HttpStatus.CREATED)
              .body(creditService.createCredit(credit.getAmount(), clientDto));
    }

    if (creditService.personalCreditExist(clientDto)) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CLIENT_HAS_CREDIT));
    }

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(creditService.createCredit(credit.getAmount(), clientDto));*/
  }

  @Override
  public Mono<ResponseEntity<Flux<Credit>>> consultCredit(String clientDocument,
                                                          ServerWebExchange exchange) {

    Flux<Credit> creditFlux = creditService.getCredits(clientDocument);

    return Mono.just(ResponseEntity.ok(creditFlux));

    //return ResponseEntity.status(HttpStatus.OK).body(creditService.getCredits(clientDocument));
  }

  @Override
  public Mono<ResponseEntity<Credit>> payCredit(Mono<CreditPay> creditPay,
                                                ServerWebExchange exchange) {

    return creditPay.flatMap(credit -> {

      if(credit.getAmount() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CREDIT_EMPTY)));
      }

      if(credit.getCreditNumber() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CREDIT_NUMBER_EMPTY)));
      }

      Mono<Boolean> creditExist = creditService.creditExist(credit.getCreditNumber());
      Mono<Boolean> overPayment = creditService.validateOverPayment(credit.getCreditNumber(), credit.getAmount());

      return creditExist.zipWith(overPayment)
              .flatMap(tuple -> {
                Boolean existCredit = tuple.getT1();
                Boolean existOverPayment = tuple.getT2();

                if(!existCredit){
                  return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CREDIT_NUMBER_INVALID)));
                }

                if(existOverPayment){
                  return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.OVERPAYMENT)));
                }

                return creditService.payCredit(credit.getCreditNumber(), credit.getAmount())
                        .map(ResponseEntity::ok);
              });

    });

    /*if (creditPaid.getAmount() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CREDIT_EMPTY));
    }

    if (creditPaid.getCreditNumber() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CREDIT_NUMBER_EMPTY));
    }

    if (!creditService.creditExist(creditPaid.getCreditNumber())) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CREDIT_NUMBER_INVALID));
    }

    if (creditService.validateOverPayment(creditPaid.getCreditNumber(), creditPaid.getAmount())) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.OVERPAYMENT));
    }

    return ResponseEntity
            .ok(creditService.payCredit(creditPaid.getCreditNumber(), creditPaid.getAmount()));*/
  }
}
