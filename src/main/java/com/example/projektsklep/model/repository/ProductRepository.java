package com.example.projektsklep.model.repository;


import com.example.projektsklep.model.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findAll(Pageable pageable);


    Optional<Product> findById(long id);



    void deleteById(Long id);


    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByAuthorId(Long authorId, Pageable pageable);
}


