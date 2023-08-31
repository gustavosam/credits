package com.microservice.credit.repository;

import com.microservice.credit.documents.MovementsDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementsRepository extends MongoRepository<MovementsDocuments, String> {
}
