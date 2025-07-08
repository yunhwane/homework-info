package com.example.homeworkips.paymentservice.point.application.service;

import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardUseCase;
import com.example.homeworkips.paymentservice.point.application.port.out.SavePointPort;
import com.example.homeworkips.paymentservice.point.domain.PointResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRewardService implements PointRewardUseCase {

    private final SavePointPort savePointPort;

    @Override
    public Mono<PointResult> rewardPoints(PointRewardCommand command) {
        return savePointPort.savePoint(command);
    }
}
