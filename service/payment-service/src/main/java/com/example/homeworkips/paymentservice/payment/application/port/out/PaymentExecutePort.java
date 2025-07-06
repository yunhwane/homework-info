package com.example.homeworkips.paymentservice.payment.application.port.out;

import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.domain.PaymentExecutionResult;
import reactor.core.publisher.Mono;

public interface PaymentExecutePort {

    Mono<PaymentExecutionResult> execute(PaymentConfirmCommand command);

}
