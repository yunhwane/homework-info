package com.example.homeworkips.memberservice.member.domain.support;

public enum Direction {
    ASC("asc"),
    DESC("desc");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Direction fromString(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.value.equalsIgnoreCase(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Unknown direction: " + value);
    }
}