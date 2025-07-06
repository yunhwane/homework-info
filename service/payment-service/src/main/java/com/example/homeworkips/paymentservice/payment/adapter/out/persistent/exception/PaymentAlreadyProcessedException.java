package com.example.homeworkips.paymentservice.payment.adapter.out.persistent.exception;


import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PaymentAlreadyProcessedException extends RuntimeException {

    private PaymentStatus status;

    public PaymentAlreadyProcessedException(PaymentStatus status) {
        super("Payment has already been processed with status: " + status);
        this.status = status;
    }

    public PaymentAlreadyProcessedException(String message) {
        super(message);
    }


}
