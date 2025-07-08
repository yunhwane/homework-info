package com.example.homeworkips.pointservice.point;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends AbstractBaseEntity {

    private Long pointId;
    private Long rewardAmount;
    private Type type;
    private String description;

    public static PointHistory of(Long pointId, Long rewardAmount, Type type, String description) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.pointId = pointId;
        pointHistory.rewardAmount = rewardAmount;
        pointHistory.type = type;
        pointHistory.description = description;
        return pointHistory;
    }
}
