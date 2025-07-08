package com.example.homeworkips.paymentservice.point.adapter.out.persistent.repository;

import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.domain.PointResult;
import reactor.core.publisher.Mono;

public interface PointRepository {

    Mono<PointResult> save(PointRewardCommand command);
}
