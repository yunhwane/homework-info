package com.example.homeworkips.paymentservice.payment.domain;

import lombok.Getter;

import java.util.Objects;


@Getter
public class PaymentConfirmationResult {

    private PaymentStatus status;
    private String message;
    private PaymentFailure paymentFailure;

    public PaymentConfirmationResult(PaymentStatus status, PaymentFailure paymentFailure) {
        this.status = Objects.requireNonNull(status, "PaymentStatus must not be null");

        this.message = switch (status) {
            case SUCCESS -> "결제 처리에 성공하였습니다.";
            case FAILURE -> "결제 처리에 실패하였습니다.";
            case UNKNOWN -> "결제 처리 결과를 알 수 없습니다.";
            default -> throw new IllegalArgumentException("Invalid payment status: " + status);
        };

        if (status == PaymentStatus.FAILURE) {
            throw new IllegalArgumentException("PaymentFailure must be provided when status is FAILURE");
        }

        this.paymentFailure = paymentFailure;
    }

    public PaymentConfirmationResult(PaymentExecutionResult executionResult) {

        this.status = Objects.requireNonNull(executionResult.paymentStatus(), "PaymentStatus must not be null");

        if (executionResult.paymentStatus() == PaymentStatus.FAILURE) {
            Objects.requireNonNull(executionResult.paymentStatus(), "PaymentFailure must not be null when paymentStatus is FAILURE");
            this.paymentFailure = executionResult.getFailure();
        }

        this.message = switch (executionResult.paymentStatus()) {
            case SUCCESS -> "결제 처리에 성공하였습니다.";
            case FAILURE -> "결제 처리에 실패하였습니다.";
            case UNKNOWN -> "결제 처리 결과를 알 수 없습니다.";
            default -> throw new IllegalArgumentException("Invalid payment status: " + executionResult.paymentStatus());
        };
    }
}