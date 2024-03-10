package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Data access exception occurred.";

        DataAccessException exception = new DataAccessException(message);

        assertEquals(message, exception.getMessage());
    }

}