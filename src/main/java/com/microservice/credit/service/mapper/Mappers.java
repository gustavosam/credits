package com.microservice.credit.service.mapper;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditRequest;
import com.microservice.credit.util.CreditDto;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Esta clase proporciona métodos para convertir una clase en otra.
 * */
public class Mappers {

  /**
   * Este método convierte la clase CreditDocument en CreditDto.
   * */
  public static CreditDto mapCreditDocumentToCreditDto(CreditDocument creditDocument) {

    CreditDto credit = new CreditDto();
    credit.setCreditNumber(creditDocument.getCreditNumber());
    credit.setCreditAmount(creditDocument.getCreditAmount());
    credit.setPendingPay(creditDocument.getPendingPay());
    credit.setAmountPaid(creditDocument.getAmountPaid());
    credit.setClientDocument(creditDocument.getClientDocument());
    credit.setCreditType(creditDocument.getCreditType());
    credit.setCreditDate(creditDocument.getCreditDate());
    return credit;
  }

  /**
   * Este método convierte la lista de creditDocument en una lista de Credit.
   * */
  public static List<Credit> mapListCreditDocToListCredit(List<CreditDocument> creditDocumentList) {
    return  creditDocumentList.stream()
            .filter(Objects::nonNull)
            .map(Mappers::mapCreditDocumentToCreditDto).collect(Collectors.toList());
  }

}
