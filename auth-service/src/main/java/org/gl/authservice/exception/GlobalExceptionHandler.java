package org.gl.authservice.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiError.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.builder()
                        .httpStatus(HttpStatus.CONFLICT)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {


        List<String> errors = ex.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage()).toList();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage())
                        .build());
    }
}
