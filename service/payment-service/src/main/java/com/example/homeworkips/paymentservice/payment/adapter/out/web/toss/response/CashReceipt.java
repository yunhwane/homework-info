package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class CashReceipt {
    private String type;
    private String receiptKey;
    private String issueNumber;
    private String receiptUrl;
    private Long amount;
    private Long taxFreeAmount;
    private String orderId;
    private String orderName;
    private String businessNumber;
    private String transactionType;
    private String issueStatus;
    private TossFailureResponse failure;
    private String customerIdentityNumber;
    private LocalDateTime requestedAt;
}
