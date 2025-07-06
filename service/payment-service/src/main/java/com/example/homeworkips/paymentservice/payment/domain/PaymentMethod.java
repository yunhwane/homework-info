package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    EASY_PAY("간편결제");

    private final String description;

    public static PaymentMethod from(String method) {
        for (PaymentMethod paymentMethod : values()) {
            if (paymentMethod.name().equalsIgnoreCase(method)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + method);
    }
}
