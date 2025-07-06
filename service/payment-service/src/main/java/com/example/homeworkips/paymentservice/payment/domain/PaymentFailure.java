package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentFailure {

    private String errorCode;
    private String message;

    public static PaymentFailure of(String errorCode, String message) {
        PaymentFailure failure = new PaymentFailure();
        failure.errorCode = errorCode;
        failure.message = message;
        return failure;
    }
}
