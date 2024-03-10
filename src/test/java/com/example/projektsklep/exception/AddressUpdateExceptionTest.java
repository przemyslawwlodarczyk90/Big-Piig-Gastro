package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressUpdateExceptionTest {

    @Test
    void testExceptionMessage() {
        String expectedMessage = "This is a test message";
        Exception exception = assertThrows(AddressUpdateException.class, () -> {
            throw new AddressUpdateException(expectedMessage);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}