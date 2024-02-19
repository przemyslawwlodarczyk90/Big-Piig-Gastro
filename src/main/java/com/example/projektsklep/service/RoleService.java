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
    // Deklaracja prywatnego pola roleRepository typu RoleRepository, służącego do operacji na rolach w bazie danych.
    private final RoleRepository roleRepository;

    // Konstruktor z adnotacją @Autowired, wstrzykujący zależność RoleRepository do serwisu.
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository; // Przypisanie wstrzykniętego repozytorium do lokalnej zmiennej.
    }

    // Metoda zwracająca listę wszystkich ról jako RoleDTO.
    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream() // Pobranie wszystkich ról z repozytorium.
                .map(this::convertToDTO) // Konwersja każdej roli na RoleDTO.
                .collect(Collectors.toList()); // Zebranie przekonwertowanych ról do listy.
    }

    // Metoda konwertująca encję roli na obiekt DTO.
    public RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleType().name()); // Tworzenie nowego RoleDTO z danymi z encji Role.
    }

    // Metoda konwertująca obiekt DTO na encję roli.
    public Role convertToEntity(RoleDTO roleDTO) {
        return Role.builder() // Użycie buildera do stworzenia nowej encji Role.
                .id(roleDTO.id()) // Ustawienie ID roli na podstawie DTO.
                .roleType(AdminOrUser.valueOf(roleDTO.roleType())) // Ustawienie typu roli (ADMIN/USER) na podstawie DTO.
                .build(); // Zbudowanie i zwrócenie nowej encji Role.
    }
}