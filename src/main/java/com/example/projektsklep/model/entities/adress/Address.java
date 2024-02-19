package com.example.projektsklep.model.entities.adress;

import com.example.projektsklep.model.entities.user.User;
import jakarta.persistence.*;
//import jdk.internal.loader.BuiltinClassLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Long userId;


    public Address(String s, String s2, String s1, String s3) {

    }


    public void setUser(User user) {
        this.userId = user.getId(); // Set foreign key based on User ID
    }


}

