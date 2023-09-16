package com.microservice.credit.service;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.model.Credit;
import com.microservice.credit.repository.CreditRepository;
import com.microservice.credit.service.mapper.MapCredit;
import com.microservice.credit.service.mapper.MapMovement;
import com.microservice.credit.util.ClientDto;
import com.microservice.credit.util.Constants;
import com.microservice.credit.util.CreditDto;
import java.time.LocalDate;

import com.microservice.credit.webclient.ClientWebClient;
import com.microservice.credit.webclient.MovementWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Esta clase implementará los métodos de la interfaz CreditService.
 * La clase contendrá la lógica de negocio para el servicio.
 * */
@Service
public class CreditServiceImpl implements CreditService {

  //@Autowired
  //private ClientFeignClient clientFeignClient;

  @Autowired
  private ClientWebClient clientWebClient;

  @Autowired
  private CreditRepository creditRepository;

  //@Autowired
  //private MovementFeignClient movementFeignClient;

  @Autowired
  private MovementWebClient movementWebClient;

  @Autowired
  private MapMovement mapMovement;

  @Autowired
  private MapCredit mapCredit;

  @Override
  public Mono<CreditDto> createCredit(Double amount, ClientDto clientDto) {

    CreditDocument credit = mapCredit.mapClientDtoToCreditDoc(clientDto, amount);
    credit.setAmountPaid(0.0);
    credit.setCreditDate(LocalDate.now());

    Mono<CreditDocument> creditDocumentMono = creditRepository.save(credit);


    return creditDocumentMono.map(creditDocument -> {

      CreditDto newCredit = mapCredit.mapCreditDocumentToCreditDto(creditDocument);
      newCredit.setMessage(Constants.CREDIT_CREATED_OK);

      movementWebClient.saveMovement(mapMovement.setValues(
              amount, clientDto.getDocument(),
              newCredit.getCreditNumber(), Constants.CREATE_CREDIT)).subscribe();

      return newCredit;
    });


    /*CreditDto newCredit = mapCredit.mapCreditDocumentToCreditDto(creditRepository.save(credit));
    newCredit.setMessage(Constants.CREDIT_CREATED_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
                    amount, clientDto.getDocument(),
                    newCredit.getCreditNumber(), Constants.CREATE_CREDIT));

    return newCredit;*/
  }

  @Override
  public Mono<ClientDto> getClient(String clientDocument) {

    return clientWebClient.getClient(clientDocument);
  }

  @Override
  public Mono<Boolean> personalCreditExist(ClientDto calledCustomer) {

    Flux<CreditDocument> creditFlux = creditRepository
            .findByClientDocument(calledCustomer.getDocument());

    return creditFlux.hasElements();

    //return !creditList.isEmpty();
  }

  @Override
  public Flux<Credit> getCredits(String clientDocument) {

    Flux<CreditDocument> creditFlux = creditRepository.findByClientDocument(clientDocument);

    //return creditFlux.map(Mappers::mapCreditDocumentToCreditDto);

    return creditFlux.map(creditDocument -> mapCredit.mapCreditDocumentToCreditDto(creditDocument));
    /*if (creditList.isEmpty()) {
      return new ArrayList<>();
    }

    return Mappers.mapListCreditDocToListCredit(creditList);*/
  }

  @Override
  public Mono<CreditDto> payCredit(String creditNumber, Double amount) {

    Mono<CreditDocument> creditDoc = creditRepository.findByCreditNumber(creditNumber);

    Mono<CreditDocument> creditDocUpdated = creditDoc.flatMap(creditDocument -> {

      creditDocument.setAmountPaid(creditDocument.getAmountPaid() + amount);
      creditDocument.setPendingPay(creditDocument.getPendingPay() - amount);
      return creditRepository.save(creditDocument);

    });


    return creditDocUpdated.map(creditUpdated -> {
      CreditDto creditPaid = mapCredit.mapCreditDocumentToCreditDto(creditUpdated);
      creditPaid.setMessage(Constants.CREDIT_PAID_OK);

      movementWebClient
              .saveMovement(mapMovement.setValues(
                      amount, creditUpdated.getClientDocument(),
                      creditUpdated.getCreditNumber(), Constants.PAY_CREDIT)).subscribe();

      return creditPaid;

    });
  }

  @Override
  public Mono<Boolean> validateOverPayment(String creditNumber, Double amount) {

    return creditRepository.findByCreditNumber(creditNumber)
            .map(creditDocument -> (creditDocument.getPendingPay() - amount) < 0)
            .defaultIfEmpty(false);
  }

  @Override
  public Mono<Boolean> creditExist(String creditNumber) {
    return creditRepository.existsById(creditNumber);
  }


}
