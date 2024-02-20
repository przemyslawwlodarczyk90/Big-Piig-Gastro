package com.example.projektsklep.service;



import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AddressService {


    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // Metoda konwertująca encję adresu na jego DTO.
    // Przyjmuje obiekt Address i zwraca odpowiadający mu obiekt AddressDTO.
    public AddressDTO convertToDTO(Address address) {
        // Używa wzorca projektowego Builder do tworzenia nowego obiektu AddressDTO z danymi z encji Address.
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity()) //
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }

    // Metoda konwertująca DTO adresu na encję adresu.
    // Przyjmuje obiekt AddressDTO i zwraca odpowiadającą mu encję Address.
    public Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setId(addressDTO.id()); //
        address.setStreet(addressDTO.street());
        address.setCity(addressDTO.city());
        address.setPostalCode(addressDTO.postalCode());
        address.setCountry(addressDTO.country());
        return address;
    }

    // Metoda zapisująca adres w bazie danych.
    // Przyjmuje AddressDTO, konwertuje go na encję Address, zapisuje i zwraca zapisany adres jako DTO.
    public AddressDTO saveAddress(AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO); //
        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }
}