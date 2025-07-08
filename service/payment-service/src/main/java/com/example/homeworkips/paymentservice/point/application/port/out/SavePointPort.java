package com.example.homeworkips.paymentservice.point.application.port.out;

import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.domain.PointResult;
import reactor.core.publisher.Mono;

public interface SavePointPort {

    Mono<PointResult> savePoint(PointRewardCommand command);
}
