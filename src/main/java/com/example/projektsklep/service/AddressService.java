package com.example.projektsklep.service;



import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Klasa serwisu oznaczona adnotacją @Service, co wskazuje na jej rolę w warstwie logiki biznesowej.
// Zarządza operacjami związanymi z adresami, takimi jak konwersja między encjami a DTO oraz zapis adresów.
@Service
public class AddressService {

    // Repozytorium adresów wstrzyknięte do serwisu, umożliwia operacje na danych adresowych w bazie danych.
    private final AddressRepository addressRepository;

    // Konstruktor z adnotacją @Autowired automatycznie wstrzykujący repozytorium adresów.
    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // Metoda konwertująca encję adresu na jego DTO.
    // Przyjmuje obiekt Address i zwraca odpowiadający mu obiekt AddressDTO.
    public AddressDTO convertToDTO(Address address) {
        // Używa wzorca projektowego Builder do tworzenia nowego obiektu AddressDTO z danymi z encji Address.
        return AddressDTO.builder()
                .id(address.getId()) // Ustawia ID.
                .street(address.getStreet()) // Ustawia nazwę ulicy.
                .city(address.getCity()) // Ustawia nazwę miasta.
                .postalCode(address.getPostalCode()) // Ustawia kod pocztowy.
                .country(address.getCountry()) // Ustawia nazwę kraju.
                .build(); // Buduje i zwraca obiekt AddressDTO.
    }

    // Metoda konwertująca DTO adresu na encję adresu.
    // Przyjmuje obiekt AddressDTO i zwraca odpowiadającą mu encję Address.
    public Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address(); // Tworzy nową instancję encji Address.
        address.setId(addressDTO.id()); // Ustawia ID.
        address.setStreet(addressDTO.street()); // Ustawia nazwę ulicy.
        address.setCity(addressDTO.city()); // Ustawia nazwę miasta.
        address.setPostalCode(addressDTO.postalCode()); // Ustawia kod pocztowy.
        address.setCountry(addressDTO.country()); // Ustawia nazwę kraju.
        return address; // Zwraca uzupełnioną encję Address.
    }

    // Metoda zapisująca adres w bazie danych.
    // Przyjmuje AddressDTO, konwertuje go na encję Address, zapisuje i zwraca zapisany adres jako DTO.
    public AddressDTO saveAddress(AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO); // Konwersja DTO na encję.
        Address savedAddress = addressRepository.save(address); // Zapis encji w bazie danych.
        return convertToDTO(savedAddress); // Zwraca zapisany adres jako DTO.
    }
}