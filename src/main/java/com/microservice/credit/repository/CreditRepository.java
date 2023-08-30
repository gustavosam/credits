package com.microservice.credit.repository;

import com.microservice.credit.documents.CreditDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreditRepository extends MongoRepository<CreditDocument, String> {

    List<CreditDocument> findByCustomerDocument(String customerDocument);

    CreditDocument findByCreditNumber(String creditNumber);

}
