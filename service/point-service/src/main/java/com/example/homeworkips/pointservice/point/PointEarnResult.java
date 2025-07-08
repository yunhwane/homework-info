package com.example.homeworkips.pointservice.point;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PointEarnResult {
    private Long pointId;
    private Long memberId;
    private Long totalRewardAmount;

    public static PointEarnResult create(Long pointId, Long memberId, Long totalRewardAmount) {
        PointEarnResult result = new PointEarnResult();
        result.pointId = pointId;
        result.memberId = memberId;
        result.totalRewardAmount = totalRewardAmount;
        return result;
    }
}
