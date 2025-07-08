package com.example.homeworkips.paymentservice.point.application.port.in;


import com.example.homeworkips.paymentservice.point.domain.PointResult;
import reactor.core.publisher.Mono;

public interface PointRewardUseCase {

    Mono<PointResult> rewardPoints(PointRewardCommand command);
}
