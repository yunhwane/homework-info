package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CheckoutResult {
    private Long amount;
    private String orderId;
    private String orderName;

    public static CheckoutResult create(Long amount, String orderId, String orderName) {
        CheckoutResult result = new CheckoutResult();
        result.amount = amount;
        result.orderId = orderId;
        result.orderName = orderName;
        return result;
    }
}
