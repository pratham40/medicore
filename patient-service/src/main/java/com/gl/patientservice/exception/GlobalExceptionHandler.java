package com.gl.patientservice.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .status(HttpStatus.valueOf(404))
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<@Nullable String> errors = ex.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage()).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(errors)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .status(HttpStatus.valueOf(500))
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }


}
