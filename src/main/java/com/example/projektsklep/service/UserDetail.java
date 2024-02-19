package com.example.projektsklep.service;

import java.util.Set;
import java.util.stream.Collectors;


import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetail implements UserDetailsService {

    // Pole przechowujące referencję do repozytorium użytkowników,
    // używane do wyszukiwania użytkownika w bazie danych.
    private final UserRepository userRepo;

    // Konstruktor klasy, wstrzykujący zależność do repozytorium użytkowników.
    // Umożliwia dostęp do operacji CRUD na danych użytkowników.
    public UserDetail(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Metoda nadpisująca metodę z interfejsu UserDetailsService.
    // Służy do załadowania danych użytkownika na podstawie podanej nazwy użytkownika,
    // w tym przypadku na podstawie adresu e-mail.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Wyszukiwanie użytkownika w bazie danych po adresie e-mail.
        // Jeśli użytkownik nie zostanie znaleziony, rzucony zostanie wyjątek UsernameNotFoundException.
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Budowanie zbioru uprawnień (authorities) użytkownika na podstawie jego ról.
        // Każda rola jest konwertowana na obiekt SimpleGrantedAuthority, co jest wymagane przez Spring Security.
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType().name()))
                .collect(Collectors.toSet());

        // Tworzenie i zwracanie obiektu UserDetails zawierającego informacje o użytkowniku,
        // w tym jego adres e-mail, hasło oraz uprawnienia.
        // Ta implementacja UserDetails pochodzi z Spring Security i jest dostosowana do jego wymagań.
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), authorities);
    }
}