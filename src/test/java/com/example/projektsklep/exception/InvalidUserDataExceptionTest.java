package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidUserDataExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Invalid email address";
        InvalidUserDataException exception = new InvalidUserDataException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }


}