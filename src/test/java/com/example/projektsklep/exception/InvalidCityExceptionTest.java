package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCityExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Invalid city name";
        InvalidCityException exception = new InvalidCityException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}