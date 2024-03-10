package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.product.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class ProducerRepositoryIntegrationTest {

    @Autowired
    private ProducerRepository producerRepository;

    @BeforeEach
    public void setup() {

        producerRepository.deleteAll();


        Producer producer1 = new Producer("Producer1");
        Producer producer2 = new Producer("Producer2");
        producerRepository.save(producer1);
        producerRepository.save(producer2);
    }

    @Test
     void testFindByName() {

        Optional<Producer> foundProducer = producerRepository.findByName("Producer1");

        assertTrue(foundProducer.isPresent(), "The producer should be found.");
    }

    @Test
     void testFindAllWithPageable() {

        Pageable pageable = PageRequest.of(0, 1);

        Page<Producer> producersPage = producerRepository.findAll(pageable);

        assertFalse(producersPage.isEmpty(), "he producer's page should not be empty.");
    }
}