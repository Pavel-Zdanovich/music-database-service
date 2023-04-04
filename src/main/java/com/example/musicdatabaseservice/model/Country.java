package com.example.musicdatabaseservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Country {

    @Id
    @Column(nullable = false, length = 2)
    @Setter
    private String code;

    @Column(length = 50)
    @Setter
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "country_language",
            joinColumns = @JoinColumn(name = "country_code"),
            inverseJoinColumns = @JoinColumn(name = "language_code")
    )
    private Set<Language> languages = new HashSet<>();

    public void addLanguage(Language language) {
        this.languages.add(language);
        language.addCountry(this);
    }
}
