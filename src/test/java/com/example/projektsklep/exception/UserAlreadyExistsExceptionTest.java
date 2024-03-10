package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "User with email 'example@email.com' already exists";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}