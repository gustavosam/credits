package com.microservice.credit.service.mapper;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.util.CreditDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapCredit {

  CreditDto mapCreditDocumentToCreditDto(CreditDocument creditDocument);
}
