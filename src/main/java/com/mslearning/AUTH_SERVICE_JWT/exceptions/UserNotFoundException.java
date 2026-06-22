package com.mslearning.AUTH_SERVICE_JWT.exceptions;

public class UserNotFoundException extends  Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
