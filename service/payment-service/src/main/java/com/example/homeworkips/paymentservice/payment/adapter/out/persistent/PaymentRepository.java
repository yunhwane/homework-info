package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;

import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import reactor.core.publisher.Mono;

public interface PaymentRepository {
    Mono<Void> save(PaymentEvent paymentEvent);
}
