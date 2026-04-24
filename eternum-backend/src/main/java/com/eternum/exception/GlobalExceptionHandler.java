package com.eternum.exception;

import com.eternum.util.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseHandler.ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseHandler.badRequest("Error de validación: " + errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseHandler.ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseHandler.badRequest(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseHandler.ApiResponse<Void>> handleException(Exception ex) {
        return ResponseHandler.internalError("Error interno del servidor: " + ex.getMessage());
    }

}
