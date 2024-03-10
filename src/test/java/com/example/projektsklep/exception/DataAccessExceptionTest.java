package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Data access exception occurred.";

        DataAccessException exception = new DataAccessException(message);

        assertEquals(message, exception.getMessage());
    }

}