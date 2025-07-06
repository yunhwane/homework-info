package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    NORMAL("일반 결제");

    private final String description;

    public static PaymentType from(String type) {
        for (PaymentType paymentType : values()) {
            if (paymentType.name().equalsIgnoreCase(type)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + type);
    }
}
