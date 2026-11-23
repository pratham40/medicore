package org.gl.authservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiError {

    private HttpStatus httpStatus;
    private Object message;
    private LocalDateTime timestamp;

}
