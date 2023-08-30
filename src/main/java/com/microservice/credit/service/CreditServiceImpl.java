package com.microservice.credit.service;

import com.microservice.credit.customer.CustomerFeignClient;
import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.documents.Customers;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditRequestPaid;
import com.microservice.credit.repository.CreditRepository;
import com.microservice.credit.service.mapper.CreditMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditServiceImpl implements CreditService{

    @Autowired
    private CustomerFeignClient customerFeignClient;

    @Autowired
    private CreditRepository creditRepository;

    @Override
    public Credit createCredit(Double amount, Customers customers) {

        CreditDocument credit = new CreditDocument();
        credit.setCreditType(customers.getCustomerType());
        credit.setCreditAmount(amount);
        credit.setCreditPendingPayment(amount);
        credit.setCreditPaidAmount(0.0);
        credit.setCustomerDocument(customers.getCustomerDocument());
        credit.setCreditDate(LocalDate.now());

        return CreditMappers.mapCreditDocumentToCredit(creditRepository.save(credit)) ;
    }

    @Override
    public Customers clientExist(String customerDocument) {

        return customerFeignClient.getCustomerById(customerDocument);
    }

    @Override
    public Boolean validateGenerateCredit(Customers calledCustomer) {

        List<CreditDocument> creditList = creditRepository.findByCustomerDocument(calledCustomer.getCustomerDocument());

        return creditList.isEmpty();
    }

    @Override
    public List<Credit> getCredits(String customerDocument) {

        List<CreditDocument> creditList = creditRepository.findByCustomerDocument(customerDocument);

        if(creditList.isEmpty()){
            return new ArrayList<>();
        }

        return CreditMappers.mapListCreditDocumentToListCredit(creditList);
    }

    @Override
    public Credit payCredit(String creditNumber, Double creditPayAmount) {

        CreditDocument creditDocument = creditRepository.findByCreditNumber(creditNumber);

        creditDocument.setCreditPaidAmount( creditDocument.getCreditPaidAmount() + creditPayAmount );
        creditDocument.setCreditPendingPayment( creditDocument.getCreditPendingPayment() - creditPayAmount );

        return CreditMappers.mapCreditDocumentToCreditUpdate( creditRepository.save(creditDocument) );
    }

    @Override
    public Boolean validatePayCredit(String creditNumber, Double creditPayAmount) {
        CreditDocument creditDocument = creditRepository.findByCreditNumber(creditNumber);

        return (creditDocument.getCreditPendingPayment() - creditPayAmount) >= 0;
    }


}
