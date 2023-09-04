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
/**
 * Esta clase implementa los métodos generados por open api.
 * */

@Service
public class CreditDelegateImpl implements CreditApiDelegate {

  @Autowired
  private CreditService creditService;

  @Override
  public ResponseEntity<Credit> createCredit(CreditRequest credit) {

    if (credit.getClientDocument() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY));
    }

    if (credit.getAmount() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CREDIT_EMPTY));
    }

    ClientDto clientDto = creditService.getClient(credit.getClientDocument());

    if (clientDto.getClientType().equalsIgnoreCase(Constants.TYPE_CLIENT_COMPANY)) {
      return ResponseEntity.status(HttpStatus.CREATED)
              .body(creditService.createCredit(credit.getAmount(), clientDto));
    }

    if (creditService.personalCreditExist(clientDto)) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CLIENT_HAS_CREDIT));
    }

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(creditService.createCredit(credit.getAmount(), clientDto));
  }

  @Override
  public ResponseEntity<List<Credit>> consultCredit(String clientDocument) {
    return ResponseEntity.status(HttpStatus.OK).body(creditService.getCredits(clientDocument));
  }

  @Override
  public ResponseEntity<Credit> payCredit(CreditPay creditPaid) {

    if (creditPaid.getAmount() == null) {
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
            .ok(creditService.payCredit(creditPaid.getCreditNumber(), creditPaid.getAmount()));
  }
}
