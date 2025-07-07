package com.example.homeworkips.memberservice.member.domain.support;

public enum SortType {
    NAME,
    VIEW_COUNT,
    REGISTERED_AT;

    public static SortType fromString(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            throw new IllegalArgumentException("Sort type must not be null or blank");
        }
        try {
            return SortType.valueOf(sortType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sort type: " + sortType, e);
        }
    }
}
