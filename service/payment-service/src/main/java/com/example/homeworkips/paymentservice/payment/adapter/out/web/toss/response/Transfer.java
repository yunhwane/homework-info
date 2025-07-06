package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Transfer {
    private String bankCode;
    private String settlementStatus;

}
