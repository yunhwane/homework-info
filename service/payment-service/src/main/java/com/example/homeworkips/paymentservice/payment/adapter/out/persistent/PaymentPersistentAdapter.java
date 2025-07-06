package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import com.example.homeworkips.paymentservice.payment.application.port.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort {

    private final PaymentRepository paymentRepository;

    @Override
    public Mono<Void> save(PaymentEvent paymentEvent) {
        return paymentRepository.save(paymentEvent);
    }
}
