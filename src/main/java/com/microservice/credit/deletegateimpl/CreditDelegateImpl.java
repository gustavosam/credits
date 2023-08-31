package com.microservice.credit.deletegateimpl;

import com.microservice.credit.api.CreditApiDelegate;
import com.microservice.credit.documents.Customers;
import com.microservice.credit.model.Credit;
import com.microservice.credit.model.CreditPaid;
import com.microservice.credit.model.CreditRequestPaid;
import com.microservice.credit.service.CreditService;
import com.microservice.credit.util.ClaseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditDelegateImpl implements CreditApiDelegate {

    @Autowired
    private CreditService creditService;



    @Override
    public ResponseEntity<Credit> createCredit(CreditRequestPaid creditRequestPaid){

        if(creditRequestPaid.getCustomerDocument() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El documento del cliente no puede ser nulo"));
        }

        if(creditRequestPaid.getCreditAmount() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El monto del crédito no puede ser nulo"));
        }

        Customers calledCustomer  = creditService.clientExist(creditRequestPaid.getCustomerDocument());

        if(calledCustomer.getCustomerDocument() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El cliente no existe, favor de registrarlo en el servicio Clientes"));
        }

        if(calledCustomer.getCustomerType().equalsIgnoreCase("COMPANY")){
            return ResponseEntity.status(HttpStatus.CREATED).body(creditService.createCredit(creditRequestPaid.getCreditAmount(), calledCustomer));
        }

        if(!creditService.validateGenerateCredit(calledCustomer)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El cliente PERSONAL ya cuenta con un crédito"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(creditService.createCredit(creditRequestPaid.getCreditAmount(), calledCustomer));
    }

    @Override
    public ResponseEntity<List<Credit>> consultCredit(String customerDocument){
        return ResponseEntity.status(HttpStatus.OK).body(creditService.getCredits(customerDocument));
    }

    @Override
    public ResponseEntity<Credit> payCredit(CreditPaid creditPaid){

        if(creditPaid.getCreditAmount() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("Ingrese el monto del crédito"));
        }

        if(creditPaid.getCreditNumber() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("Ingrese el número de crédito a pagar"));
        }

        if(! creditService.creditExist(creditPaid.getCreditNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El número de crédito es invalido"));
        }

        if( !creditService.validatePayCredit(creditPaid.getCreditNumber(), creditPaid.getCreditAmount())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("Verifica tu deuda, estás pagando de más"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(creditService.payCredit(creditPaid.getCreditNumber(), creditPaid.getCreditAmount()));

    }

}
