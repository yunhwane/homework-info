package com.example.homeworkips.paymentservice.payment.domain;

import lombok.Getter;

import java.util.Objects;


@Getter
public class PaymentConfirmationResult {

    private final PaymentStatus status;
    private final String message;
    private final PaymentFailure paymentFailure;

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

}