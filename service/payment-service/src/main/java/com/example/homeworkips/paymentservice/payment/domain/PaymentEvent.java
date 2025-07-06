package com.example.homeworkips.paymentservice.payment.domain;


import com.example.homeworkips.paymentservice.payment.application.port.in.CheckoutCommand;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PaymentEvent {

    private Long id;
    private Long buyerId;
    private String orderName;
    private String orderId;
    private String paymentKey;
    private PaymentType paymentType;
    private PaymentMethod paymentMethod;
    private LocalDateTime approvedAt;
    private Long amount;
    private PaymentStatus paymentStatus;

    public static PaymentEvent create(CheckoutCommand checkoutCommand) {
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.amount = checkoutCommand.getAmount();
        paymentEvent.buyerId = checkoutCommand.getBuyerId();
        paymentEvent.orderName = checkoutCommand.getOrderName();
        paymentEvent.orderId = checkoutCommand.getIdempotencyKey();
        paymentEvent.paymentStatus = PaymentStatus.NOT_STARTED;
        return paymentEvent;
    }
}
