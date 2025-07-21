package com.yesul.exception.handler;

public class TravelPlanNotFoundException extends RuntimeException {
    public TravelPlanNotFoundException(String message) {
        super(message);
    }
}
