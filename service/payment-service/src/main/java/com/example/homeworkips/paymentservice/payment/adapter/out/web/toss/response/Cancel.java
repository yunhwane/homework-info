package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@ToString
public class Cancel {
    private Long cancelAmount;
    private String cancelReason;
    private Long taxFreeAmount;
    private Integer taxExemptionAmount;
    private Long refundableAmount;
    private Long transferDiscountAmount;
    private Long easyPayDiscountAmount;
    private LocalDateTime canceledAt;
    private String transactionKey;
    private String receiptKey;
    private String cancelStatus;
    private String cancelRequestId;
}
