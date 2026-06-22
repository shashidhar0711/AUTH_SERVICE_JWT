package com.mslearning.AUTH_SERVICE_JWT.dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
