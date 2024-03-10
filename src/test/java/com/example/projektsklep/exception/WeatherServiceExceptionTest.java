package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Error occurred while fetching weather data from external service";
        WeatherServiceException exception = new WeatherServiceException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}