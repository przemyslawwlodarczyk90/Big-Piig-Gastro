package com.example.projektsklep.service;



import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.entities.adress.Address;
import org.springframework.stereotype.Service;



@Service
public class AddressService {



    public Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setId(addressDTO.id()); //
        address.setStreet(addressDTO.street());
        address.setCity(addressDTO.city());
        address.setPostalCode(addressDTO.postalCode());
        address.setCountry(addressDTO.country());
        return address;
    }


}