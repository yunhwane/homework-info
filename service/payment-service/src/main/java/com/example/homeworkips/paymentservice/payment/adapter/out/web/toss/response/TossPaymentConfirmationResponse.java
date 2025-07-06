package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class TossPaymentConfirmationResponse {
    private String version;
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method;
    private Long totalAmount;
    private Long balanceAmount;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private Boolean useEscrow;
    private String lastTransactionKey;
    private Long suppliedAmount;
    private Long vat;
    private Boolean cultureExpense;
    private Long taxFreeAmount;
    private Integer taxExemptionAmount;
    private List<Cancel> cancels;
    private Boolean isPartialCancelable;
    private Card card;
    private VirtualAccount virtualAccount;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Object metadata;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private TossFailureResponse failure;
    private CashReceipt cashReceipt;
    private List<CashReceipt> cashReceipts;
    private Discount discount;
}
