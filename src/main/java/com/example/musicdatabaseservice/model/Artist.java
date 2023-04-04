package com.example.musicdatabaseservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Artist {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Country country;

}
