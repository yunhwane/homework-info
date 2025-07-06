package com.example.homeworkips.paymentservice.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@ToString
public class PaymentExecutionResult {
    private String paymentKey;
    private String orderId;
    private PaymentExtraDetails extraDetails;
    private PaymentFailure failure;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryable;

    public static PaymentExecutionResult create(String paymentKey, String orderId, PaymentExtraDetails extraDetails, PaymentFailure failure, Boolean isSuccess, Boolean isFailure, Boolean isUnknown, Boolean isRetryable) {
        return new PaymentExecutionResult(paymentKey, orderId, extraDetails, failure, isSuccess, isFailure, isUnknown, isRetryable);
    }

    public PaymentStatus paymentStatus() {
        if (isSuccess) return PaymentStatus.SUCCESS;
        if (isFailure) return PaymentStatus.FAILURE;
        if (isUnknown) return PaymentStatus.UNKNOWN;

        // Kotlin의 error() 함수와 동일
        throw new IllegalStateException(
                String.format("결제 (orderId: %s) 는 올바르지 않은 결제 상태입니다.", orderId)
        );
    }
}

