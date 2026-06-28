package com.mslearning.AUTH_SERVICE_JWT.exceptions;

import com.mslearning.AUTH_SERVICE_JWT.models.Session;

public class SessionNotFoundException extends  Exception{
    public SessionNotFoundException(String message) {
        super(message);
    }
}
