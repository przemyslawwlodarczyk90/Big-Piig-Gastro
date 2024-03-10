package com.example.projektsklep.model.entities.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;


 class ProducerTest {



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testEmptyConstructor() {
        Producer producer = new Producer();
        assertNull(producer.getId());
        assertNull(producer.getName());
    }

    @Test
     void testConstructorWithName() {
        String expectedName = "Test Producer";
        Producer producer = new Producer(expectedName);
        assertNull(producer.getId()); // ID is auto-generated
        assertEquals(expectedName, producer.getName());
    }

}