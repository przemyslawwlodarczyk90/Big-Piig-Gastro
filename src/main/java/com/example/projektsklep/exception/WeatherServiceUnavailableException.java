package com.example.projektsklep.exception;

public class WeatherServiceUnavailableException extends RuntimeException {

    public WeatherServiceUnavailableException(String message) {
        super(message);
    }
}