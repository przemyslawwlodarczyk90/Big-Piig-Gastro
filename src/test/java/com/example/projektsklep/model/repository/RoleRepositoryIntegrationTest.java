package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

@Transactional

 class RoleRepositoryIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void setup() {

        roleRepository.deleteAll();


        Role adminRole = new Role(null, AdminOrUser.ADMIN, new HashSet<>());
        roleRepository.save(adminRole);

        Role userRole = new Role(null, AdminOrUser.USER, new HashSet<>());
        roleRepository.save(userRole);
    }


    @Test
     void testAddNewRole() {
        Role newRole = new Role();
        newRole.setRoleType(AdminOrUser.ADMIN);
        Role savedRole = roleRepository.save(newRole);

        assertNotNull(savedRole.getId(), "The id of the saved role should not be null.");
        assertEquals(AdminOrUser.ADMIN, savedRole.getRoleType(), "The saved role should have the type ADMIN");
    }
}