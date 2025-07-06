package com.example.homeworkips.paymentservice.payment.adapter.in.web.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest{
    private Long buyerId = 1L;
    private Long amount = 50000L;
    private String orderName = "50000원 포인트 충전";
    private String seed = LocalDateTime.now().toString();
}
