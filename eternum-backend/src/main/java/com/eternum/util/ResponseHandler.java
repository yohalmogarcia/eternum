package com.eternum.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * Utility class for standardized JSON responses.
 * All JSON responses must be processed through this class per organizational standards.
 */
public class ResponseHandler {

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;
        private Integer statusCode;
        private String error;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Operación exitosa");
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, String error) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message, "Bad Request");
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message, "Not Found");
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message, "Unauthorized");
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message, "Forbidden");
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message, "Internal Server Error");
    }

}
