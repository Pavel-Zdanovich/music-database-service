package com.example.musicdatabaseservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Genre {

    @Id
    private Integer id;

    @Column(nullable = false, length = 15)
    private String name;

}
