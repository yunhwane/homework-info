package com.example.homeworkips.paymentservice.point.adapter.out.persistent;

import com.example.homeworkips.paymentservice.point.adapter.out.persistent.repository.PointRepository;
import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.application.port.out.SavePointPort;
import com.example.homeworkips.paymentservice.point.domain.PointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class PointPersistentAdapter implements SavePointPort {

    private final PointRepository pointRepository;

    @Override
    public Mono<PointResult> savePoint(PointRewardCommand command) {
        return pointRepository.save(command);
    }
}
