package com.microservice.credit.repository;

import com.microservice.credit.documents.CreditDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Esta interfaz contiene los métodos necesarios para el crud de creditos.
 * */
public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {

  Flux<CreditDocument> findByClientDocument(String clientDocument);

  Mono<CreditDocument> findByCreditNumber(String creditNumber);

}
