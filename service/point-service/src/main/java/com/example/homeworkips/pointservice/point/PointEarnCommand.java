package com.example.homeworkips.pointservice.point;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PointEarnCommand {
    private Long memberId;
    private Long rewardAmount;
    private String typeCode;
    private String description;


    public static PointEarnCommand of(Long memberId, Long rewardAmount, String typeCode, String description) {
        PointEarnCommand command = new PointEarnCommand();
        command.memberId = memberId;
        command.rewardAmount = rewardAmount;
        command.typeCode = typeCode;
        command.description = description;
        return command;
    }
}
