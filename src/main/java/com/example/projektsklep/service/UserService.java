package com.example.projektsklep.service;

import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.RoleDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.AddressRepository;
import com.example.projektsklep.model.repository.RoleRepository;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, AddressService addressService, AddressRepository addressRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserDTO);
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserDTO);
    }


    @Transactional
    public void saveUser(UserDTO userDTO, AddressDTO addressDTO, AdminOrUser role) {
        Address address = null;

        if (addressDTO != null) {
            address = addressService.convertToEntity(addressDTO);
            address = addressRepository.save(address);
        }

        User user = convertToUser(userDTO);


        if (address != null) {
            user.setAddress(address);
        }

        String encodedPassword = passwordEncoder.encode(userDTO.password());
        user.setPasswordHash(encodedPassword);
        Set<Role> roles = new HashSet<>();
        if (role != null) {
            Role userRole = roleRepository.findByRoleType(role)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            roles.add(userRole);
        }
        user.setRoles(roles);

        user = userRepository.save(user);

        convertToUserDTO(user);
    }


    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserDTO);
    }

    public List<UserDTO> findUsersByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCaseContaining(lastName).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }


    public void updateUserProfileAndAddress(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());


        Address address = user.getAddress() != null ? user.getAddress() : new Address();
        AddressDTO addressDTO = userDTO.address();
        if (addressDTO != null) {
            address.setStreet(addressDTO.street());
            address.setCity(addressDTO.city());
            address.setPostalCode(addressDTO.postalCode());
            address.setCountry(addressDTO.country());
            address = addressRepository.save(address); // Zapis adresu w bazie danych.
            user.setAddress(address); // Przypisanie adresu do użytkownika.
        }

        // Zapis zmodyfikowanego użytkownika w bazie danych.
        userRepository.save(user);

        // Konwersja zaktualizowanego użytkownika na DTO i zwrócenie go.
        convertToUserDTO(user);
    }

    // Konwersja obiektu User na DTO.
    private UserDTO convertToUserDTO(User user) {
        AddressDTO addressDTO = null;
        // Jeśli użytkownik ma przypisany adres, konwertuje go na DTO.
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            addressDTO = AddressDTO.builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .city(address.getCity())
                    .postalCode(address.getPostalCode())
                    .country(address.getCountry())
                    .build();
        }

        // Tworzy i zwraca DTO użytkownika z uwzględnieniem jego adresu i ról.
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(null) // Hasło nie jest uwzględniane w DTO.
                .address(addressDTO)
                .roles(user.getRoles().stream()
                        .map(role -> new RoleDTO(role.getId(), role.getRoleType().name()))
                        .collect(Collectors.toSet()))
                .build();
    }

    // Konwersja z UserDTO na encję User.
    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        // Ustawia dane użytkownika na podstawie DTO.
        user.setId(userDTO.id());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());

        // Konwertuje adres z DTO na encję i przypisuje go do użytkownika, jeśli istnieje.
        user.setAddress(userDTO.address() != null ? addressService.convertToEntity(userDTO.address()) : null);
        return user;
    }



    // Inicjalizacja nowego UserDTO z pustymi wartościami.
    public UserDTO initializeNewUserDTO() {
        AddressDTO addressDTO = new AddressDTO(0L, "", "", "", "");
        return UserDTO.builder()
                .id(null)
                .firstName("")
                .lastName("")
                .email("")
                .password("")
                .address(addressDTO)
                .roles(new HashSet<>())
                .build();
    }



    // Pobranie szczegółów użytkownika dla panelu na podstawie e-maila.
    public UserDTO getUserDetailsForPanel(String email) {
        UserDTO userDTO = findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        logUserAndAddressDetails(userDTO); // Logowanie informacji o użytkowniku i jego adresie.

        return userDTO;
    }


    private void logUserAndAddressDetails(UserDTO userDTO) {
        System.out.println("UserDTO: " + userDTO);
        if (userDTO.address() != null) {
            AddressDTO address = userDTO.address();
            System.out.println("Address: " + address.street() + ", " + address.city() + ", " + address.postalCode() + ", " + address.country());
        } else {
            System.out.println("Address is null");
        }
    }

    // Przygotowanie UserDTO do edycji na podstawie e-maila.
    public UserDTO prepareUserDTOForEdit(String email) {
        UserDTO userDTO = findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        // Jeśli DTO nie ma przypisanego adresu, przypisuje pusty AddressDTO.
        if (userDTO.address() == null) {
            AddressDTO emptyAddress = new AddressDTO(null, "", "", "", "");
            userDTO = new UserDTO(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(),
                    userDTO.password(), emptyAddress, userDTO.roles());
        }

        return userDTO;
    }
}