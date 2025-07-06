package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand;
import reactor.core.publisher.Mono;

public interface PaymentStatusUpdateRepository {
    Mono<Boolean> updatePaymentStatusToExecuting(String orderId, String paymentKey);

    Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command);
}
