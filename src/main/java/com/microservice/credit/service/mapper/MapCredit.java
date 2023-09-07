package com.microservice.credit.service.mapper;

import com.microservice.credit.documents.CreditDocument;
import com.microservice.credit.util.ClientDto;
import com.microservice.credit.util.CreditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz de mapstruct para mapeo de clases.
 * */
@Mapper(componentModel = "spring")
public interface MapCredit {

  CreditDto mapCreditDocumentToCreditDto(CreditDocument creditDocument);

  @Mapping(target = "creditType", source = "clientDto.clientType")
  @Mapping(target = "creditAmount", source = "amount")
  @Mapping(target = "pendingPay", source = "amount")
  @Mapping(target = "clientDocument", source = "clientDto.document")
  CreditDocument mapClientDtoToCreditDoc(ClientDto clientDto, Double amount);
}
