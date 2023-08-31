package com.microservice.credit.service;

import com.microservice.credit.customer.CustomerFeignClient;
import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.documents.Customers;
import com.microservice.credit.documents.MovementsDocuments;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditRequestPaid;
import com.microservice.credit.repository.CreditRepository;
import com.microservice.credit.repository.MovementsRepository;
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

    @Autowired
    private MovementsRepository movementsRepository;

    @Override
    public Credit createCredit(Double amount, Customers customers) {

        CreditDocument credit = new CreditDocument();
        credit.setCreditType(customers.getCustomerType());
        credit.setCreditAmount(amount);
        credit.setCreditPendingPayment(amount);
        credit.setCreditPaidAmount(0.0);
        credit.setCustomerDocument(customers.getCustomerDocument());
        credit.setCreditDate(LocalDate.now());

        Credit newCredit = CreditMappers.mapCreditDocumentToCredit(creditRepository.save(credit));

        //GUARDAR MOVIMIENTO DE LA CRECIÓN DE CRÉDITO
        MovementsDocuments movementCredit = new MovementsDocuments();
        movementCredit.setMovementType("ALTA CRÉDITO");
        movementCredit.setCustomerDocument(customers.getCustomerDocument());
        movementCredit.setCreditId(newCredit.getCreditNumber());
        movementCredit.setAmount(amount);
        movementCredit.setMovementDate(LocalDate.now());

        movementsRepository.save(movementCredit);

        return newCredit;
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

        Credit creditPaid = CreditMappers.mapCreditDocumentToCreditUpdate( creditRepository.save(creditDocument) );

        //EL PAGO DEL CRÉDITO GENERA UN MOVIMIENTO
        MovementsDocuments movementCredit = new MovementsDocuments();
        movementCredit.setMovementType("PAGO CRÉDITO");
        movementCredit.setCustomerDocument(creditDocument.getCustomerDocument());
        movementCredit.setCreditId(creditDocument.getCreditNumber());
        movementCredit.setAmount(creditPayAmount);
        movementCredit.setMovementDate(LocalDate.now());

        movementsRepository.save(movementCredit);

        return creditPaid;
    }

    @Override
    public Boolean validatePayCredit(String creditNumber, Double creditPayAmount) {
        CreditDocument creditDocument = creditRepository.findByCreditNumber(creditNumber);

        return (creditDocument.getCreditPendingPayment() - creditPayAmount) >= 0;
    }


}
