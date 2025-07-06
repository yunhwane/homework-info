package com.example.homeworkips.paymentservice.payment.application.port.in;


import com.example.homeworkips.paymentservice.payment.domain.CheckoutResult;
import reactor.core.publisher.Mono;

public interface CheckoutUseCase {
    Mono<CheckoutResult> checkout(CheckoutCommand command);
}
