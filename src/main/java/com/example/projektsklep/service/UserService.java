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
import org.springframework.beans.factory.annotation.Autowired;
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

    // Metoda zwracająca stronicowaną listę użytkowników jako DTO.
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserDTO); // Konwersja użytkowników do DTO.
    }

    // Metoda wyszukująca użytkownika po ID i zwracająca go jako DTO, jeśli istnieje.
    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserDTO); // Konwersja użytkownika do DTO.
    }

    // Metoda zapisująca nowego użytkownika w bazie danych z przypisaniem adresu i roli.
    @Transactional // Zapewnia wykonanie operacji w ramach transakcji.
    public UserDTO saveUser(UserDTO userDTO, AddressDTO addressDTO, AdminOrUser role) {
        Address address = null;
        // Konwersja i zapis adresu, jeśli podano DTO.
        if (addressDTO != null) {
            address = addressService.convertToEntity(addressDTO);
            address = addressRepository.save(address);
        }

        User user = convertToUser(userDTO); // Konwersja DTO użytkownika na encję.

        // Przypisanie adresu do użytkownika.
        if (address != null) {
            user.setAddress(address);
        }

        // Kodowanie hasła użytkownika.
        String encodedPassword = passwordEncoder.encode(userDTO.password());
        user.setPasswordHash(encodedPassword);

        // Przypisanie roli do użytkownika.
        Set<Role> roles = new HashSet<>();
        if (role != null) {
            Role userRole = roleRepository.findByRoleType(role)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            roles.add(userRole);
        }
        user.setRoles(roles);

        // Zapis użytkownika w bazie danych.
        user = userRepository.save(user);

        return convertToUserDTO(user); // Konwersja zapisanego użytkownika na DTO.
    }

    // Metoda usuwająca użytkownika po ID.
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Metoda wyszukująca użytkownika po adresie e-mail i zwracająca go jako DTO.
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserDTO);
    }

    // Metoda wyszukująca użytkowników po nazwisku, ignorując wielkość liter.
    public List<UserDTO> findUsersByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCaseContaining(lastName).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    // Metoda aktualizująca profil użytkownika lub admina.
    public UserDTO updateUserProfileOrAdmin(Long userId, UserDTO userDTO, boolean isAdmin, String authenticatedUserEmail) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Aktualizacja danych użytkownika.
        updateUserFields(existingUser, userDTO);

        userRepository.save(existingUser); // Zapis zmian w bazie danych.

        return convertToUserDTO(existingUser); // Zwrócenie zaktualizowanego użytkownika jako DTO.
    }

    // Aktualizacja profilu i adresu użytkownika na podstawie przekazanego adresu e-mail i danych DTO.
    public UserDTO updateUserProfileAndAddress(String email, UserDTO userDTO) {
        // Wyszukiwanie użytkownika w bazie danych po adresie e-mail. Jeśli użytkownik nie istnieje, rzuca wyjątek.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Aktualizacja danych osobowych użytkownika.
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());

        // Ustawienie lub aktualizacja adresu użytkownika. Jeśli adres już istnieje, aktualizuje go; jeśli nie - tworzy nowy.
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
        return convertToUserDTO(user);
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

    // Aktualizacja pól użytkownika na podstawie danych z DTO.
    private void updateUserFields(User user, UserDTO userDTO) {
        user.setEmail(userDTO.email());
        if (userDTO.password() != null) {
            user.setPasswordHash(userDTO.password());
        }
        if (userDTO.address() != null) {
            Address address = addressService.convertToEntity(userDTO.address());
            user.setAddress(address);
        }
    }

    // Tworzenie UserDTO na podstawie danych wejściowych.
    public UserDTO createUserDTO(UserDTO userDTO, AddressDTO addressDTO) {
        return UserDTO.builder()
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .address(addressDTO)
                .build();
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

    // Rejestracja użytkownika z określoną rolą.
    @Transactional // Zapewnia, że cała operacja rejestracji jest wykonywana w ramach jednej transakcji.
    public UserDTO registerUser(UserDTO userDTO, String roleTypeStr) throws Exception {
        AdminOrUser roleType;
        try {
            roleType = AdminOrUser.valueOf(roleTypeStr.toUpperCase()); // Próba konwersji stringa na enum.
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid role type: " + roleTypeStr); // Rzucenie wyjątku, jeśli podana rola jest nieprawidłowa.
        }

        return saveUser(userDTO, userDTO.address(), roleType); // Zapisanie użytkownika z wykorzystaniem metody saveUser.
    }

    // Pobranie szczegółów użytkownika dla panelu na podstawie e-maila.
    public UserDTO getUserDetailsForPanel(String email) {
        UserDTO userDTO = findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        logUserAndAddressDetails(userDTO); // Logowanie informacji o użytkowniku i jego adresie.

        return userDTO;
    }

    // Logowanie informacji o użytkowniku i adresie.
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