package com.example.homeworkips.paymentservice.payment.application.service;

import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmUseCase;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentExecutePort;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdatePort;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentValidationPort;
import com.example.homeworkips.paymentservice.payment.domain.PaymentConfirmationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class PaymentConfirmService implements PaymentConfirmUseCase {

    private final PaymentStatusUpdatePort paymentStatusUpdatePort;
    private final PaymentValidationPort paymentValidationPort;
    private final PaymentExecutePort paymentExecutePort;
    private final PaymentErrorHandler paymentErrorHandler;

    @Override
    public Mono<PaymentConfirmationResult> confirm(PaymentConfirmCommand command) {
        return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.getOrderId(), command.getPaymentKey())
                .filterWhen(it -> paymentValidationPort.isValid(command.getOrderId(), command.getAmount()))
                .flatMap(it -> paymentExecutePort.execute(command))
                .flatMap(it ->
                        paymentStatusUpdatePort.updatePaymentStatus(PaymentStatusUpdateCommand.from(it)
                        ).thenReturn(it)
                )
                .map(it -> new PaymentConfirmationResult(
                        it.paymentStatus(),
                        it.getFailure()
                ))
                .onErrorResume(error -> paymentErrorHandler.handlePaymentConfirmationError(error, command));
    }
}
