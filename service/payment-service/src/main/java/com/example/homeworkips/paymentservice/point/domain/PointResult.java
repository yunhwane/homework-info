package com.example.homeworkips.paymentservice.point.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PointResult {
    private PointType type;
    private Long amount;
    private String orderId;
    private Long buyerId;

    public static PointResult create(PointType type, Long amount, String orderId, Long buyerId) {
        PointResult result = new PointResult();
        result.type = type;
        result.amount = amount;
        result.orderId = orderId;
        result.buyerId = buyerId;
        return result;
    }
}
