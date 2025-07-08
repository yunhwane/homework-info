package com.example.homeworkips.paymentservice.point.application.port.in;


import lombok.Getter;

@Getter
public class PointRewardCommand {
    private String orderId;
    private Long buyerId;
    private Long paymentAmount;

    public static PointRewardCommand create(String orderId, Long buyerId, Long paymentAmount) {
        PointRewardCommand command = new PointRewardCommand();
        command.orderId = orderId;
        command.buyerId = buyerId;
        command.paymentAmount = paymentAmount;
        return command;
    }
}
