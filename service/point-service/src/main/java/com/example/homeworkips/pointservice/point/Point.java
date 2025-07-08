package com.example.homeworkips.pointservice.point;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends AbstractBaseEntity {

    private Long memberId;
    private Long rewardAmount;

    public static Point of(Long memberId, Long rewardAmount) {
        Point point = new Point();
        point.memberId = memberId;
        point.rewardAmount = rewardAmount;
        return point;
    }
}
