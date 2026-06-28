package com.mslearning.AUTH_SERVICE_JWT.exceptions;

public class UserLoggedOutException extends Exception{
    public UserLoggedOutException(String message) {
        super(message);
    }
}
