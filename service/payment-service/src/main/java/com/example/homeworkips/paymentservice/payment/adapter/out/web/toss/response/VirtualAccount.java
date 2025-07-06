package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class VirtualAccount {
    private String accountType;
    private String accountNumber;
    private String bankCode;
    private String customerName;
    private LocalDateTime dueDate;
    private String refundStatus;
    private Boolean expired;
    private String settlementStatus;
    private RefundReceiveAccount refundReceiveAccount;
    private String secret;
}
