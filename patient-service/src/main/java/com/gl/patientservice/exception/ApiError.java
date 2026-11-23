package com.gl.patientservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiError {
    private HttpStatus status;
    private Object message;
    private LocalDateTime timestamp;
}
