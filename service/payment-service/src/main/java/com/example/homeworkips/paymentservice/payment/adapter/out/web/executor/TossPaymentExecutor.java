package com.example.homeworkips.paymentservice.payment.adapter.out.web.executor;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TossPaymentExecutor {

    private final WebClient tossPaymentWebClient;
    private final String uri = "/v1/payments/confirm";

    public Mono<String> execute(String paymentKey, String orderId, String amount) {
        return tossPaymentWebClient.post()
                .uri(uri)
                .bodyValue("""
                {
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "amount": "%s"
                }
                """.formatted(paymentKey, orderId, amount))
                .retrieve()
                .bodyToMono(String.class);
    }
}
