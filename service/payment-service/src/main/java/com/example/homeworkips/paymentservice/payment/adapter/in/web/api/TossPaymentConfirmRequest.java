package com.example.homeworkips.paymentservice.payment.adapter.in.web.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TossPaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private String amount;
}
