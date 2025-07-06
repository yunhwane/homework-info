package com.example.homeworkips.paymentservice.payment.application.port.out;

import com.example.homeworkips.paymentservice.payment.domain.PaymentExecutionResult;
import com.example.homeworkips.paymentservice.payment.domain.PaymentExtraDetails;
import com.example.homeworkips.paymentservice.payment.domain.PaymentFailure;
import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;

import java.util.Optional;


public record PaymentStatusUpdateCommand(
        String paymentKey,
        String orderId,
        PaymentStatus status,
        PaymentExtraDetails extraDetails,
        PaymentFailure failure
) {

    public static PaymentStatusUpdateCommand from(PaymentExecutionResult paymentExecutionResult) {
        return new PaymentStatusUpdateCommand(
                Optional.ofNullable(paymentExecutionResult.getPaymentKey()).orElse(""),
                Optional.ofNullable(paymentExecutionResult.getOrderId()).orElse(""),
                Optional.ofNullable(paymentExecutionResult.paymentStatus()).orElse(PaymentStatus.UNKNOWN),
                paymentExecutionResult.getExtraDetails(),
                paymentExecutionResult.getFailure()
        );
    }

    public PaymentStatusUpdateCommand {
        validateStatus(status);
        validateStatusConstraints(status, extraDetails, failure);
    }

    private static void validateStatus(PaymentStatus status) {
        if (status != PaymentStatus.SUCCESS &&
                status != PaymentStatus.FAILURE &&
                status != PaymentStatus.UNKNOWN) {
            throw new IllegalArgumentException(
                    String.format("결제 상태 (status: %s) 는 올바르지 않은 결제 상태입니다.", status)
            );
        }
    }

    private static void validateStatusConstraints(PaymentStatus status,
                                                  PaymentExtraDetails extraDetails,
                                                  PaymentFailure failure) {
        if (status == PaymentStatus.SUCCESS) {
            if (extraDetails == null) {
                throw new IllegalArgumentException(
                        "PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails 는 null 이 되면 안됩니다."
                );
            }
        } else if (status == PaymentStatus.FAILURE) {
            if (failure == null) {
                throw new IllegalArgumentException(
                        "PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다."
                );
            }
        }
    }
}