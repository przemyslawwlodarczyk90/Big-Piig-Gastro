package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Optional;

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
        // Usunięcie wszystkich ról, aby uniknąć konfliktów i naruszeń integralności.
        roleRepository.deleteAll();

        // Dodanie unikalnych ról
        Role adminRole = new Role(null, AdminOrUser.ADMIN, new HashSet<>());
        roleRepository.save(adminRole);

        Role userRole = new Role(null, AdminOrUser.USER, new HashSet<>());
        roleRepository.save(userRole);
    }

//    @Test
//    public void testFindByRoleTypeAdmin() {
//        Optional<Role> foundRole = roleRepository.findByRoleType(AdminOrUser.ADMIN);
//        assertTrue(foundRole.isPresent(), "Rola admina powinna zostać znaleziona.");
//        foundRole.ifPresent(role ->
//                assertEquals(AdminOrUser.ADMIN, role.getRoleType(), "Znaleziona rola powinna mieć typ ADMIN.")
//        );
//    }

    @Test
     void testAddNewRole() {
        Role newRole = new Role();
        newRole.setRoleType(AdminOrUser.ADMIN);
        Role savedRole = roleRepository.save(newRole);

        assertNotNull(savedRole.getId(), "Id zapisanej roli nie powinno być null.");
        assertEquals(AdminOrUser.ADMIN, savedRole.getRoleType(), "Zapisana rola powinna mieć typ ADMIN");
    }
}