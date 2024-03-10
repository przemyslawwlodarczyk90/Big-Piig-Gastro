package com.example.projektsklep.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String expectedMessage = "Product with ID 123 not found";
        ProductNotFoundException exception = new ProductNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}