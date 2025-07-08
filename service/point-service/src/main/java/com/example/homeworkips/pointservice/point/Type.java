package com.example.homeworkips.pointservice.point;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {

    EARN("EARN", "적립"),
    USE("USE", "사용");

    private final String code;
    private final String description;
}
