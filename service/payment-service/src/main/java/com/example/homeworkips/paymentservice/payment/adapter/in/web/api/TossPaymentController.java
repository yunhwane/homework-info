package com.example.homeworkips.paymentservice.payment.adapter.in.web.api;


import com.example.homeworkips.paymentservice.common.ApiResponse;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmUseCase;
import com.example.homeworkips.paymentservice.payment.domain.PaymentConfirmationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/toss")
@RequiredArgsConstructor
public class TossPaymentController {

    private final PaymentConfirmUseCase paymentConfirmUseCase;

    @PostMapping("/confirm")
    public Mono<ResponseEntity<ApiResponse<PaymentConfirmationResult>>> confirm(@RequestBody TossPaymentConfirmRequest request) {
        return paymentConfirmUseCase.confirm( PaymentConfirmCommand.create(
                request.getPaymentKey(),
                request.getOrderId(),
                request.getAmount()
        )).map(it -> ResponseEntity.ok(
                ApiResponse.with(
                        HttpStatus.OK,
                        "OK",
                                it
                        )));
    }
}
