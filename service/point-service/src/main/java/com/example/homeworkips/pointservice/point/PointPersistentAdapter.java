package com.example.homeworkips.pointservice.point;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointPersistentAdapter implements SavePointPort {

    private final JpaPointRepository jpaPointRepository;
    private final JpaPointHistoryRepository jpaPointHistoryRepository;

    @Override
    public PointEarnResult save(PointEarnCommand pointEarnCommand) {

        Point point = jpaPointRepository.findByMemberId(pointEarnCommand.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + pointEarnCommand.getMemberId()));

        PointHistory pointHistory = PointHistory.of(point.getId(), point.getRewardAmount(), Type.USE, pointEarnCommand.getDescription());
        jpaPointHistoryRepository.save(pointHistory);

        return PointEarnResult.create(point.getId(), point.getMemberId(), point.getRewardAmount());
    }
}
