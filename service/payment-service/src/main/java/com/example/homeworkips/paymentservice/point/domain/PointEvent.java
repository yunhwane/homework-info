package com.example.homeworkips.paymentservice.point.domain;

import java.time.Instant;

public record PointEvent(
        String orderId,
        String paymentKey,
        Long buyerId,
        Long paymentAmount,
        Instant occurredAt
) {
    public static PointEvent from(String orderId, String paymentKey, Long buyerId, Long paymentAmount) {
        return new PointEvent(
                orderId,
                paymentKey,
                buyerId,
                paymentAmount,
                Instant.now()
        );
    }
}
