package com.example.musicdatabaseservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Language {

    @Id
    @Column(nullable = false, length = 2)
    @Getter
    @Setter
    private String code;

    @ManyToMany(mappedBy = "languages")
    private Set<Country> countries = new HashSet<>();

    public void addCountry(Country country) {
        this.countries.add(country);
    }

}
