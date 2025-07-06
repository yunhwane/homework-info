package com.example.homeworkips.paymentservice.payment.application.port.out;

import reactor.core.publisher.Mono;

public interface PaymentValidationPort {
    Mono<Boolean> isValid(String orderId, Long amount);
}
