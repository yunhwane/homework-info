package com.example.homeworkips.paymentservice.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdempotencyCreator {

    public static <T> String create(T data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
