package com.mslearning.AUTH_SERVICE_JWT.dtos;

import com.mslearning.AUTH_SERVICE_JWT.models.Role;
import lombok.Data;

import java.util.Set;

@Data
public class JwtUserDto {
    private Long userId;
    private String email;
    private Set<Role> role;
}
