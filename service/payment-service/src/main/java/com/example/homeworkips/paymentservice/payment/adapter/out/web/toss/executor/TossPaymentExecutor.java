package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.executor;


import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException;
import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.exception.TossPaymentError;
import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response.TossFailureResponse;
import com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.response.TossPaymentConfirmationResponse;
import com.example.homeworkips.paymentservice.payment.application.port.in.PaymentConfirmCommand;
import com.example.homeworkips.paymentservice.payment.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentExecutor implements PaymentExecutor {

    private final WebClient tossPaymentWebClient;
    private final String uri = "/v1/payments/confirm";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<PaymentExecutionResult> execute(PaymentConfirmCommand command) {
        return tossPaymentWebClient.post()
                .uri(uri)
                .bodyValue("""
                {
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "amount": "%s"
                }
                """.formatted(command.getPaymentKey(), command.getOrderId(), command.getAmount()))
                .retrieve()
                .onStatus(
                        statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                        response -> response.bodyToMono(TossFailureResponse.class)
                                .flatMap(failureResponse -> {
                                    TossPaymentError error = TossPaymentError.from(failureResponse.getCode());
                                    return Mono.error(PSPConfirmationException.of(
                                            error.name(),
                                            error.getDescription(),
                                            error.isSuccess(),
                                            error.isFailure(),
                                            error.isUnknown(),
                                            error.isRetryableError(),
                                            null
                                    ));
                                })
                )
                .bodyToMono(TossPaymentConfirmationResponse.class)
                .map(it ->
                        PaymentExecutionResult.create(
                                it.getPaymentKey(),
                                it.getOrderId(),
                                PaymentExtraDetails.create(
                                        PaymentType.from(it.getType()),
                                        PaymentMethod.from(it.getMethod()),
                                        it.getApprovedAt(),
                                        it.getOrderName(),
                                        PSPConfirmationStatus.from(it.getStatus()),
                                        it.getTotalAmount(),
                                        serialize(it)
                                ),
                                null,
                                true,
                                false,
                                false,
                                false

                        )
                )
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1)
                                .filter(throwable -> {
                                    if (throwable instanceof PSPConfirmationException pspException) {
                                        return pspException.getIsRetryableError();
                                    }

                                    return throwable instanceof TimeoutException;
                                })
                        .doBeforeRetry(retrySignal -> {
                            boolean isUnknown = false;
                            if (retrySignal.failure() instanceof PSPConfirmationException pspException) {
                                isUnknown = pspException.getIsUnknown();
                            }
                            log.info("Retrying count: {}, isUnknown: {}",
                                    retrySignal.totalRetries(),
                                    isUnknown);
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure())

                );
    }

    private String serialize(TossPaymentConfirmationResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payment response to JSON", e);
        }
    }
}
