package com.yesul.exception.handler;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message, Exception e) {
        super(message);
    }
}
