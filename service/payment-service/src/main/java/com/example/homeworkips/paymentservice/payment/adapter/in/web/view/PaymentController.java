package com.example.homeworkips.paymentservice.payment.adapter.in.web.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class PaymentController {


    @GetMapping("/success")
    public Mono<String> successPage() {
        return Mono.just("success");
    }

    @GetMapping("/fail")
    public Mono<String> failPage() {
        return Mono.just("fail");
    }
}
