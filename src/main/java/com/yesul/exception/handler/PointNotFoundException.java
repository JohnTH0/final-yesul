package com.yesul.exception.handler;

public class PointNotFoundException extends RuntimeException {
    public PointNotFoundException(String message) {
        super(message);
    }
}
