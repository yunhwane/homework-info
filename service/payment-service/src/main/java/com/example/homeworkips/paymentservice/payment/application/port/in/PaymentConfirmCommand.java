package com.example.homeworkips.paymentservice.payment.application.port.in;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentConfirmCommand {
    private String paymentKey;
    private String orderId;
    private Long amount;

    public static PaymentConfirmCommand create(String paymentKey, String orderId, Long amount) {
        PaymentConfirmCommand command = new PaymentConfirmCommand();
        command.paymentKey = paymentKey;
        command.orderId = orderId;
        command.amount = amount;
        return command;
    }
}
