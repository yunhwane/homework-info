package com.example.homeworkips.paymentservice.payment.application.port.in;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CheckoutCommand {
    private Long buyerId;
    private Long amount;
    private String orderName;
    private String idempotencyKey;

    public static CheckoutCommand create(Long buyerId, Long amount, String orderName, String idempotencyKey) {
        CheckoutCommand command = new CheckoutCommand();
        command.buyerId = buyerId;
        command.amount = amount;
        command.orderName = orderName;
        command.idempotencyKey = idempotencyKey;
        return command;
    }
}
