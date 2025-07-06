package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    EASY_PAY("간편결제");

    private final String description;
}
