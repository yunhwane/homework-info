package com.example.homeworkips.paymentservice.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    EASY_PAY("간편결제"),
    CREDIT_CARD("카드"),
    BANK_TRANSFER("계좌이체"),
    VIRTUAL_ACCOUNT("가상계좌"),
    MOBILE_PAYMENT("휴대폰"),
    CULTURE_GIFT_CARD("문화상품권"),
    BOOK_CULTURE_GIFT_CARD("도서문화상품권"),
    GAME_CULTURE_GIFT_CARD("게임문화상품권");

    private final String description;

    public static PaymentMethod from(String method) {
        for (PaymentMethod paymentMethod : values()) {
            if (paymentMethod.description.equals(method)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + method);
    }
}
