package com.mslearning.AUTH_SERVICE_JWT.dtos;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponseDto {
    private String message;
    private HttpStatus status;
}
