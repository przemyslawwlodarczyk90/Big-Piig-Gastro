package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceUnavailableExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Weather service is temporarily unavailable";
        WeatherServiceUnavailableException exception = new WeatherServiceUnavailableException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}