package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderUpdateExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Unable to update order due to insufficient stock";
        OrderUpdateException exception = new OrderUpdateException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}