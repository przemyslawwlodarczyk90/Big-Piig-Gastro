package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryExceptionTest {

    @Test
    void testConstructorWithMessageAndCause() {
        String errorMessage = "Error in operation";
        Throwable rootCause = new NullPointerException("Category is null");

        CategoryException exception = new CategoryException(errorMessage, rootCause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }


}