package com.example.homeworkips.paymentservice.payment.application.port.out;

import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import reactor.core.publisher.Mono;

public interface SavePaymentPort {

    Mono<Void> save(PaymentEvent paymentEvent);
}
