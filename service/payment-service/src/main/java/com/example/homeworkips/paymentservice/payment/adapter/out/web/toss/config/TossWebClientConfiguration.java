package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Base64;

@Configuration
public class TossWebClientConfiguration {

    @Value("${toss.payment.api.base-url}")
    private String baseUrl;

    @Value("${toss.payment.api.secretKey}")
    private String secretKey;

    @Bean
    public WebClient tossPaymentWebClient() {
        String encodedSecretKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(reactorClientHttpConnector())
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
    }

    private ClientHttpConnector reactorClientHttpConnector() {
        ConnectionProvider provider = ConnectionProvider.builder("toss-payment")
                .build();

        return new ReactorClientHttpConnector(HttpClient.create(provider));
    }

}
