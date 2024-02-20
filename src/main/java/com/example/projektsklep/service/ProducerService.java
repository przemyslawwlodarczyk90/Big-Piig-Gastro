package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.ProducerDTO;
import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.Producer;
import com.example.projektsklep.model.repository.ProducerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProducerService {
    // Inicjalizacja loggera dla klasy, umożliwiająca logowanie działań.
    private static final Logger log = LoggerFactory.getLogger(ProducerService.class);


    private final ProducerRepository authorRepository;



    public ProducerService(ProducerRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Zapisanie producenta do bazy danych z zachowaniem transakcyjności.
    // Transakcyjność oznacza, że cała operacja zapisu będzie traktowana jako jedna całość.
    @Transactional
    public ProducerDTO saveAuthor(ProducerDTO authorDTO) {
        log.info("Attempting to save author: {}", authorDTO);
        // Sprawdzenie, czy producent o danej nazwie już istnieje w bazie danych.
        Optional<Producer> existingAuthor = authorRepository.findByName(authorDTO.name());
        if (existingAuthor.isPresent()) {
            log.info("Author already exists: {}", authorDTO.name());
            // Jeśli istnieje, zwraca jego dane jako DTO bez tworzenia nowego wpisu.
            return convertToAuthorDTO(existingAuthor.get());
        }

        // Utworzenie nowej encji producenta i zapisanie jej w bazie.
        Producer author = new Producer();
        author.setName(authorDTO.name());
        author = authorRepository.save(author);
        log.info("Author saved successfully: {}", author.getName());
        // Zwrócenie zapisanego producenta jako DTO.
        return convertToAuthorDTO(author);
    }

    // Pobranie wszystkich producentów w formie stronicowanej.
    public Page<ProducerDTO> findAllAuthorsPageable(Pageable pageable) {
        log.info("Fetching authors with pageable: {}", pageable);
        // Użycie repozytorium do pobrania stronicowanej listy producentów i konwersja na DTO.
        return authorRepository.findAll(pageable)
                .map(this::convertToAuthorDTO);
    }

    // Prywatna metoda pomocnicza do konwersji encji Producenta na DTO.
    private ProducerDTO convertToAuthorDTO(Producer author) {
        log.info("Converting author entity to DTO: {}", author.getName());
        return new ProducerDTO(author.getId(), author.getName());
    }

    // Pobranie listy wszystkich producentów i zwrócenie jako lista DTO.
    public List<ProducerDTO> findAll() {
        log.info("Fetching all authors");
        // Pobranie wszystkich producentów z bazy, konwersja na DTO, i zebranie do listy.
        return authorRepository.findAll().stream().map(this::convertToAuthorDTO).collect(Collectors.toList());
    }
}