package com.mslearning.AUTH_SERVICE_JWT.dtos;

import lombok.Data;

@Data
public class LogoutResponseDto {
    private RequestStatus requestStatus;
    private String message;
}
