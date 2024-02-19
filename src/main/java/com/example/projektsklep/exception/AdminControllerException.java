package com.example.projektsklep.exception;

import org.springframework.http.HttpStatus;

public class AdminControllerException extends Exception {

    public AdminControllerException(String message) {
        super(message);
    }

    public AdminControllerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    private HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }
}