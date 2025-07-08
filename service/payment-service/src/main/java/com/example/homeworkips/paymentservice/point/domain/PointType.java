package com.example.homeworkips.paymentservice.point.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    REWARD("REWARD"),
    USE("USE");

    private final String type;
}
