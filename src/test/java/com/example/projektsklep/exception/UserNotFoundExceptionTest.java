package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "User with ID 123 not found"; // Example message
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}