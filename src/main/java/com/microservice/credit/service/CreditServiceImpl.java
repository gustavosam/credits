package com.microservice.credit.service;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.feignclient.ClientFeignClient;
import com.microservice.credit.feignclient.MovementFeignClient;
import com.microservice.credit.model.Credit;
import com.microservice.credit.repository.CreditRepository;
import com.microservice.credit.service.mapper.MapperMovement;
import com.microservice.credit.service.mapper.Mappers;
import com.microservice.credit.util.Client;
import com.microservice.credit.util.Constants;
import com.microservice.credit.util.CreditDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Esta clase implementará los métodos de la interfaz CreditService.
 * La clase contendrá la lógica de negocio para el servicio.
 * */
@Service
public class CreditServiceImpl implements CreditService {

  @Autowired
  private ClientFeignClient clientFeignClient;

  @Autowired
  private CreditRepository creditRepository;

  @Autowired
  private MovementFeignClient movementFeignClient;

  @Override
  public CreditDto createCredit(Double amount, Client client) {

    CreditDocument credit = new CreditDocument();
    credit.setCreditType(client.getClientType());
    credit.setCreditAmount(amount);
    credit.setPendingPay(amount);
    credit.setAmountPaid(0.0);
    credit.setClientDocument(client.getDocument());
    credit.setCreditDate(LocalDate.now());

    CreditDto newCredit = Mappers.mapCreditDocumentToCreditDto(creditRepository.save(credit));
    newCredit.setMessage(Constants.CREDIT_CREATED_OK);

    movementFeignClient.saveMovement(MapperMovement.setValues(
                    amount, client.getDocument(),
                    newCredit.getCreditNumber(), Constants.CREATE_CREDIT));

    return newCredit;
  }

  @Override
  public Client getClient(String clientDocument) {

    return clientFeignClient.getClient(clientDocument);
  }

  @Override
  public Boolean personalCreditExist(Client calledCustomer) {

    List<CreditDocument> creditList = creditRepository
            .findByClientDocument(calledCustomer.getDocument());

    return !creditList.isEmpty();
  }

  @Override
  public List<Credit> getCredits(String clientDocument) {

    List<CreditDocument> creditList = creditRepository.findByClientDocument(clientDocument);

    if (creditList.isEmpty()) {
      return new ArrayList<>();
    }

    return Mappers.mapListCreditDocToListCredit(creditList);
  }

  @Override
  public CreditDto payCredit(String creditNumber, Double amount) {

    CreditDocument creditDoc = creditRepository.findByCreditNumber(creditNumber);

    creditDoc.setAmountPaid(creditDoc.getAmountPaid() + amount);
    creditDoc.setPendingPay(creditDoc.getPendingPay() - amount);

    CreditDto creditPaid = Mappers.mapCreditDocumentToCreditDto(creditRepository.save(creditDoc));
    creditPaid.setMessage(Constants.CREDIT_PAID_OK);

    movementFeignClient
            .saveMovement(MapperMovement.setValues(
                    amount, creditDoc.getClientDocument(),
                    creditDoc.getCreditNumber(), Constants.PAY_CREDIT));

    return creditPaid;
  }

  @Override
  public Boolean validateOverPayment(String creditNumber, Double amount) {
    CreditDocument creditDocument = creditRepository.findByCreditNumber(creditNumber);

    return (creditDocument.getPendingPay() - amount) < 0;
  }

  @Override
  public Boolean creditExist(String creditNumber) {
    return creditRepository.existsById(creditNumber);
  }


}
