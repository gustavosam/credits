package com.microservice.credit.service.mapper;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditRequestPaid;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreditMappers {

    public static CreditDocument mapCreditRequestPaidToCreditDocument(CreditRequestPaid creditRequestPaid){

        CreditDocument creditDocument = new CreditDocument();
        creditDocument.setCreditAmount(creditRequestPaid.getCreditAmount());
        creditDocument.setCustomerDocument(creditRequestPaid.getCustomerDocument());

        return creditDocument;
    }

    public static Credit mapCreditDocumentToCredit(CreditDocument creditDocument){

        Credit credit = new Credit();
        credit.setCreditNumber(creditDocument.getCreditNumber());
        credit.setCreditAmount(creditDocument.getCreditAmount());
        credit.setCreditPendingPayment(creditDocument.getCreditPendingPayment());
        credit.setCreditPaidAmount(creditDocument.getCreditPaidAmount());
        credit.setCustomerDocument(creditDocument.getCustomerDocument());
        credit.setCreditType(creditDocument.getCreditType());
        credit.setCreditDate(LocalDate.now());
        return credit;
    }

    public static List<Credit> mapListCreditDocumentToListCredit(List<CreditDocument> creditDocumentList){
        return  creditDocumentList.stream().filter(Objects::nonNull).map(CreditMappers::mapCreditDocumentToCredit).collect(Collectors.toList());
    }


    public static Credit mapCreditDocumentToCreditUpdate(CreditDocument creditDocument){

        Credit credit = new Credit();
        credit.setCreditNumber(creditDocument.getCreditNumber());
        credit.setCreditAmount(creditDocument.getCreditAmount());
        credit.setCreditPendingPayment(creditDocument.getCreditPendingPayment());
        credit.setCreditPaidAmount(creditDocument.getCreditPaidAmount());
        credit.setCustomerDocument(creditDocument.getCustomerDocument());
        credit.setCreditType(creditDocument.getCreditType());
        credit.setCreditDate(creditDocument.getCreditDate());
        return credit;
    }

}
