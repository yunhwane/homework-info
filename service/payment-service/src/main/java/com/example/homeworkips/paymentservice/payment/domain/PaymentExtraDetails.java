package com.example.homeworkips.paymentservice.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@ToString
public class PaymentExtraDetails {
    private PaymentType type;
    private PaymentMethod method;
    private LocalDateTime approvedAt;
    private String orderName;
    private PSPConfirmationStatus pspConfirmationStatus;
    private Long amount;
    private String pspRawData;

    public static PaymentExtraDetails create(PaymentType type, PaymentMethod method, LocalDateTime approvedAt, String orderName, PSPConfirmationStatus pspConfirmationStatus, Long amount, String pspRawData) {
        return new PaymentExtraDetails(type, method, approvedAt, orderName, pspConfirmationStatus, amount, pspRawData);
    }
}
