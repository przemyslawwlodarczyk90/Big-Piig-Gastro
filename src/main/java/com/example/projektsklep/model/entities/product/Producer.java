package com.example.projektsklep.model.entities.product;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Producer() {

    }

    public Producer(String name) {
        this.name = name;
    }

}