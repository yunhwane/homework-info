package com.example.homeworkips.memberservice.member.adapter.in.api;

import com.example.homeworkips.memberservice.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch error: {}", e.getMessage());
        String errorMessage = String.format("Invalid value '%s' for parameter '%s'",
                e.getValue(), e.getName());

        return ResponseEntity.badRequest()
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, errorMessage, null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, e.getMessage(), null));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleNumberFormatException(NumberFormatException e) {
        log.warn("Number format error: {}", e.getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, "Invalid number format", null));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.with(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unexpected exception occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.with(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null));
    }
}

