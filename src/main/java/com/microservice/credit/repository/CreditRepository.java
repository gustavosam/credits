package com.microservice.credit.repository;

import com.microservice.credit.documents.CreditDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Esta interfaz contiene los métodos necesarios para el crud de creditos.
 * */
public interface CreditRepository extends MongoRepository<CreditDocument, String> {

  List<CreditDocument> findByClientDocument(String clientDocument);

  CreditDocument findByCreditNumber(String creditNumber);

}
