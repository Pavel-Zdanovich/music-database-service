package com.example.musicdatabaseservice.deezer.model;

import lombok.Data;

import java.util.Set;

@Data
public class Track {

    private Integer id;

    private Boolean readable;

    private String title;

    private Integer rank;

    private String preview;

    private Set<String> availableCountries;

    private Artist artist;

    private Album album;

}
