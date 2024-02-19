package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.product.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findByName(String name);

    Page<Producer> findAll(Pageable pageable);


}