package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long>  {
    Optional<Role> findByRoleType(AdminOrUser roleType);

}
