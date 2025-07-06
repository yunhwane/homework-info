package com.example.homeworkips.paymentservice.payment.adapter.in.web.view;


import com.example.homeworkips.paymentservice.common.IdempotencyCreator;
import com.example.homeworkips.paymentservice.payment.adapter.in.web.request.CheckoutRequest;
import com.example.homeworkips.paymentservice.payment.application.port.in.CheckoutCommand;
import com.example.homeworkips.paymentservice.payment.application.port.in.CheckoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutUseCase checkoutUseCase;

    @GetMapping("/")
    public Mono<String> checkout(CheckoutRequest request, Model model) {
        return checkoutUseCase.checkout(CheckoutCommand.create(
                request.getBuyerId(),
                request.getAmount(),
                request.getOrderName(),
                IdempotencyCreator.create(request)
        )).map(checkoutResult -> {
            model.addAttribute("amount", checkoutResult.getAmount());
            model.addAttribute("orderId", checkoutResult.getOrderId());
            model.addAttribute("orderName", checkoutResult.getOrderName());
            return "checkout";
        });
    }
}
