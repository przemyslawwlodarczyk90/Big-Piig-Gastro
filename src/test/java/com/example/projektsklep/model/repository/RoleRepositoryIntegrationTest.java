package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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