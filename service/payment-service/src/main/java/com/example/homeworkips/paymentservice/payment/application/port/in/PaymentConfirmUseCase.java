package com.example.homeworkips.paymentservice.payment.application.port.in;


import com.example.homeworkips.paymentservice.payment.domain.PaymentConfirmationResult;
import reactor.core.publisher.Mono;

public interface PaymentConfirmUseCase {

    Mono<PaymentConfirmationResult> confirm(PaymentConfirmCommand command);
}
