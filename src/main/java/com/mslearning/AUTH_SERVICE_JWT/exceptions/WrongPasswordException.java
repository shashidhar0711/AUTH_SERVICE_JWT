package com.mslearning.AUTH_SERVICE_JWT.exceptions;

public class WrongPasswordException extends Exception{
    public WrongPasswordException(String message) {
        super(message);
    }
}
