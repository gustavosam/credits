package com.microservice.credit.service;

import com.microservice.credit.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditServiceImplTest {

  @Mock
  private CreditRepository creditRepository;

  @InjectMocks
  private CreditServiceImpl creditService;

  @Test
  public void testCreditExist() {
    String creditNumber = "credit_number";

    when(creditRepository.existsById(creditNumber)).thenReturn(Mono.just(true));

    Mono<Boolean> creditExistsMono = creditService.creditExist(creditNumber);

    StepVerifier.create(creditExistsMono)
            .expectNext(true)
            .expectComplete()
            .verify();
  }
}