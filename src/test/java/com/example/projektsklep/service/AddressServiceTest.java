package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressServiceTest {

    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService();
    }

    @Test
    void testConvertToEntity_ShouldConvertAddressDTOToEntity() {

        AddressDTO addressDTO = AddressDTO.builder()
                .id(1L)
                .street("Test Street")
                .city("Test City")
                .postalCode("12345")
                .country("Test Country")
                .build();


        Address address = addressService.convertToEntity(addressDTO);


        assertEquals(addressDTO.id(), address.getId());
        assertEquals(addressDTO.street(), address.getStreet());
        assertEquals(addressDTO.city(), address.getCity());
        assertEquals(addressDTO.postalCode(), address.getPostalCode());
        assertEquals(addressDTO.country(), address.getCountry());
    }
}
