package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderNotFoundExceptionTest {

    @Test
    void testConstructorWithOrderId() {

        String orderId = "123";
        String expectedMessage = "Zamówienie o ID " + orderId + " nie zostało znalezione";
        OrderNotFoundException exception = new OrderNotFoundException(orderId);

        assertEquals(expectedMessage, exception.getMessage());
    }
}