package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingExceptionTest {

    @Test
    public void testConstructorWithMessageAndStatusCode() {
        int errorCode = 400;
        String errorMessage = "Bad request";

        ErrorHandlingException exception = new ErrorHandlingException(errorCode, errorMessage);

        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testErrorHandlingExceptionExtendsRuntimeException() {
        ErrorHandlingException exception = new ErrorHandlingException(404, "Not found");
        assertTrue(exception instanceof RuntimeException);
    }

}