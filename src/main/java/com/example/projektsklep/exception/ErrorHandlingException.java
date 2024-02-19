package com.example.projektsklep.exception;

public class ErrorHandlingException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public ErrorHandlingException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}