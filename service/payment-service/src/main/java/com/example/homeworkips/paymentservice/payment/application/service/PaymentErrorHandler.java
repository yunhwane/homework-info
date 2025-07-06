package com.example.homeworkips.paymentservice.payment.application.service;


import com.example.homeworkips.paymentservice.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException;
import com.example.homeworkips.paymentservice.payment.adapter.out.persistent.exception.PaymentValidationException;
import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdatePort;
import com.example.homeworkips.paymentservice.payment.domain.PaymentConfirmationResult;
import com.example.homeworkips.paymentservice.payment.domain.PaymentFailure;
import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentErrorHandler {

    private final PaymentStatusUpdatePort paymentStatusUpdatePort;

    public Mono<PaymentConfirmationResult> handlePaymentConfirmationError(Throwable error, PaymentConfirmCommand command) {

        PaymentStatus status;
        PaymentFailure failure;

        switch (error) {

            case PSPConfirmationException pspException -> {
                status = pspException.getPaymentStatus();
                failure = PaymentFailure.of(pspException.getErrorCode(), pspException.getErrorMessage());
            }

            case PaymentValidationException validationException -> {
                status = PaymentStatus.FAILURE;
                failure = PaymentFailure.of(
                        validationException.getClass().getSimpleName(),
                        validationException.getMessage() != null ? validationException.getMessage() : ""
                );
            }
            case PaymentAlreadyProcessedException alreadyProcessedException -> {

                PaymentFailure alreadyProcessedFailure = PaymentFailure.of(
                        alreadyProcessedException.getClass().getSimpleName(),
                        alreadyProcessedException.getMessage() != null ? alreadyProcessedException.getMessage() : ""
                );
                return Mono.just(new PaymentConfirmationResult(
                        alreadyProcessedException.getStatus(),
                        alreadyProcessedFailure
                ));
            }
            case TimeoutException timeoutException -> {
                status = PaymentStatus.UNKNOWN;
                failure = PaymentFailure.of(
                        timeoutException.getClass().getSimpleName(),
                        timeoutException.getMessage() != null ? timeoutException.getMessage() : ""
                );
            }
            default -> {
                status = PaymentStatus.UNKNOWN;
                failure = PaymentFailure.of(
                        error.getClass().getSimpleName(),
                        error.getMessage() != null ? error.getMessage() : ""
                );
            }
        }

        PaymentStatusUpdateCommand paymentStatusUpdateCommand = new PaymentStatusUpdateCommand(
                command.getPaymentKey(),
                command.getOrderId(),
                status,
                null,
                failure
        );

        // 상태 업데이트 후 결과 반환
        return paymentStatusUpdatePort.updatePaymentStatus(paymentStatusUpdateCommand)
                .map(result -> new PaymentConfirmationResult(status, failure));
    }
}
