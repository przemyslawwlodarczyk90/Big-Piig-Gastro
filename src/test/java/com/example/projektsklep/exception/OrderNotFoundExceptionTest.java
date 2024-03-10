package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderNotFoundExceptionTest {

    @Test
    void testConstructorWithOrderId() {
        String expectedMessage = "Zamówienie o ID 123 nie zostało znalezione";
        OrderNotFoundException exception = new OrderNotFoundException("123");

        assertEquals(expectedMessage, exception.getMessage());
    }
}