package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByLastNameIgnoreCaseContaining(String lastName);




    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);

    void deleteById(Long id);
}

