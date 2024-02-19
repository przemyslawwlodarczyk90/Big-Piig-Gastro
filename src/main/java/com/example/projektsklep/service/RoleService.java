package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.RoleDTO;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleType().name());
    }

    public Role convertToEntity(RoleDTO roleDTO) {
        return Role.builder()
                .id(roleDTO.id())
                .roleType(AdminOrUser.valueOf(roleDTO.roleType()))
                .build();
    }
}