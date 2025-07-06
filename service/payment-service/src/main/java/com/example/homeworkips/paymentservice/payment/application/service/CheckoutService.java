package com.example.homeworkips.paymentservice.payment.application.service;


import com.example.homeworkips.paymentservice.payment.domain.CheckoutResult;
import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import com.example.homeworkips.paymentservice.payment.application.port.in.CheckoutCommand;
import com.example.homeworkips.paymentservice.payment.application.port.in.CheckoutUseCase;
import com.example.homeworkips.paymentservice.payment.application.port.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUseCase {

    private final SavePaymentPort savePaymentPort;

    @Override
    public Mono<CheckoutResult> checkout(CheckoutCommand command) {
        return savePaymentPort.save(PaymentEvent.create(command))
                .thenReturn(CheckoutResult.create(command.getAmount(), command.getIdempotencyKey(), command.getOrderName()));
    }

}
