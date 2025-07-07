package com.example.homeworkips.memberservice.common;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> with(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = status.value();
        response.message = message;
        response.data = data;
        return response;
    }
}
