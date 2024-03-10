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
public class ProducerRepositoryIntegrationTest {

    @Autowired
    private ProducerRepository producerRepository;

    @BeforeEach
    public void setup() {
        // Wyczyszczenie bazy danych przed każdym testem
        producerRepository.deleteAll();

        // Przygotowanie danych testowych
        Producer producer1 = new Producer("Producer1");
        Producer producer2 = new Producer("Producer2");
        producerRepository.save(producer1);
        producerRepository.save(producer2);
    }

    @Test
    public void testFindByName() {
        // Próba znalezienia producenta po nazwie
        Optional<Producer> foundProducer = producerRepository.findByName("Producer1");

        // Weryfikacja, że producent został znaleziony
        assertTrue(foundProducer.isPresent(), "Producent powinien zostać znaleziony.");
    }

    @Test
    public void testFindAllWithPageable() {
        // Utworzenie obiektu Pageable do stronicowania
        Pageable pageable = PageRequest.of(0, 1);

        // Pobranie stronicowanej listy producentów
        Page<Producer> producersPage = producerRepository.findAll(pageable);

        // Weryfikacja, że strona zawiera producentów
        assertFalse(producersPage.isEmpty(), "Strona producentów nie powinna być pusta.");
    }
}