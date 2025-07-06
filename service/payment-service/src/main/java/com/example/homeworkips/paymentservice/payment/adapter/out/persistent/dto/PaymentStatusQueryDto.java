package com.example.homeworkips.paymentservice.payment.adapter.out.persistent.dto;


import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;

public record PaymentStatusQueryDto (
        Long orderId,
        PaymentStatus paymentStatus
){
}
