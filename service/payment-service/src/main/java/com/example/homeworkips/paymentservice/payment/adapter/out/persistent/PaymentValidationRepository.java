package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;

import reactor.core.publisher.Mono;

public interface PaymentValidationRepository {

    Mono<Boolean> isValid(String orderId, Long amount);
}
