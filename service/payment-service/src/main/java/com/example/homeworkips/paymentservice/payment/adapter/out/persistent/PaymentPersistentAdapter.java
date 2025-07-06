package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdatePort;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentValidationPort;
import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import com.example.homeworkips.paymentservice.payment.application.port.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentPersistentAdapter implements SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort {

    private final PaymentRepository paymentRepository;
    private final PaymentStatusUpdateRepository paymentStatusUpdateRepository;
    private final PaymentValidationRepository paymentValidationRepository;

    @Override
    public Mono<Void> save(PaymentEvent paymentEvent) {
        return paymentRepository.save(paymentEvent);
    }

    @Override
    public Mono<Boolean> updatePaymentStatusToExecuting(String orderId, String paymentKey) {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey);
    }

    @Override
    public Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command) {
        return paymentStatusUpdateRepository.updatePaymentStatus(command);
    }

    @Override
    public Mono<Boolean> isValid(String orderId, Long amount) {
        return paymentValidationRepository.isValid(orderId, amount);
    }
}
