package com.mslearning.AUTH_SERVICE_JWT.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Session extends BaseModel {
    private String token;
    private Date expireAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
    @ManyToOne
    private User user;
}
