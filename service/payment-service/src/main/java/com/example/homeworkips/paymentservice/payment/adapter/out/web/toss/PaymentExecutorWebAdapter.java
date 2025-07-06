package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss;


import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.executor.PaymentExecutor;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentExecutePort;
import com.example.homeworkips.paymentservice.payment.domain.PaymentExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentExecutorWebAdapter implements PaymentExecutePort {

    private final PaymentExecutor paymentExecutor;

    @Override
    public Mono<PaymentExecutionResult> execute(PaymentConfirmCommand command) {
        return paymentExecutor.execute(command);
    }
}
