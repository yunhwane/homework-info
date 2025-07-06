package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentStatus {
    NOT_STARTED("결제 승인 시작 전"),
    EXECUTING("결제 승인 중"),
    SUCCESS("결제 승인 성공"),
    FAILURE("결제 승인 실패"),
    UNKNOWN("결제 승인 알 수 없는 상태");

    private final String description;
}
