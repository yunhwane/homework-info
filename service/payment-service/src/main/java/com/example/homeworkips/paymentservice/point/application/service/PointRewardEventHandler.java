package com.example.homeworkips.paymentservice.point.application.service;

import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardUseCase;
import com.example.homeworkips.paymentservice.point.domain.PointEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRewardEventHandler {

   private final PointRewardUseCase pointRewardUseCase;

    @Async
    @TransactionalEventListener
    public void handlePointRewardEvent(PointEvent pointEvent) {
        pointRewardUseCase.rewardPoints(
                PointRewardCommand.create(
                        pointEvent.orderId(),
                        pointEvent.buyerId(),
                        pointEvent.paymentAmount()
                )
        ).subscribe();
    }
}
