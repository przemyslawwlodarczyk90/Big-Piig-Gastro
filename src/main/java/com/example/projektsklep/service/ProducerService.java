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
    private static final Logger log = LoggerFactory.getLogger(ProducerService.class);
    private final ProducerRepository authorRepository;

    @Autowired
    public ProducerService(ProducerRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public ProducerDTO saveAuthor(ProducerDTO authorDTO) {
        log.info("Attempting to save author: {}", authorDTO);
        Optional<Producer> existingAuthor = authorRepository.findByName(authorDTO.name());
        if (existingAuthor.isPresent()) {
            log.info("Author already exists: {}", authorDTO.name());
            return convertToAuthorDTO(existingAuthor.get());
        }

        Producer author = new Producer();
        author.setName(authorDTO.name());
        author = authorRepository.save(author);
        log.info("Author saved successfully: {}", author.getName());
        return convertToAuthorDTO(author);
    }

    public Page<ProducerDTO> findAllAuthorsPageable(Pageable pageable) {
        log.info("Fetching authors with pageable: {}", pageable);
        return authorRepository.findAll(pageable)
                .map(this::convertToAuthorDTO);
    }

    private ProducerDTO convertToAuthorDTO(Producer author) {
        log.info("Converting author entity to DTO: {}", author.getName());
        return new ProducerDTO(author.getId(), author.getName());
    }

    public List<ProducerDTO> findAll() {
        log.info("Fetching all authors");
        return authorRepository.findAll().stream().map(this::convertToAuthorDTO).collect(Collectors.toList());
    }


}