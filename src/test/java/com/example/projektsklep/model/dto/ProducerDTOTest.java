package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProducerDTOTest {

    @Test
    void testNameValid() {
        ProducerDTO producerDTO = ProducerDTO.builder()
                .name("Valid Name")
                .build();

        assertTrue(isNameValid(producerDTO.name()));
    }

    @Test
    void testNameTooShort() {
        ProducerDTO producerDTO = ProducerDTO.builder()
                .name("V")
                .build();

        assertFalse(isNameValid(producerDTO.name()));
    }

    @Test
    void testNameTooLong() {
        StringBuilder longName = new StringBuilder();
        longName.append("a".repeat(51));

        ProducerDTO producerDTO = ProducerDTO.builder()
                .name(longName.toString())
                .build();

        assertFalse(isNameValid(producerDTO.name()));
    }

    @Test
    void testNameBlank() {
        ProducerDTO producerDTO = ProducerDTO.builder()
                .name("")
                .build();

        assertFalse(isNameValid(producerDTO.name()));
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.length() >= 2 && name.length() <= 50;
    }
}