package com.example.projektsklep.model.repository;


import com.example.projektsklep.model.entities.adress.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}