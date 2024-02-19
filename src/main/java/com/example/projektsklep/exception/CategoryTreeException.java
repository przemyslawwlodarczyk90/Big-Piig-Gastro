package com.example.projektsklep.exception;

public class CategoryTreeException extends RuntimeException {

    public CategoryTreeException(String message) {
        super(message);
    }

    public CategoryTreeException(String message, Throwable cause) {
        super(message, cause);
    }
}