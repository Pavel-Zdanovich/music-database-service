package com.example.deezerpullingservice.deezer.model;

import lombok.Data;

import java.util.Date;

@Data
public class Album {

    private Integer id;

    private String title;

    private Integer genreId;

    private Page<Genre> genres;

    private Integer nbTracks;

    private Integer fans;

    private Date releaseDate;

    private String recordType;

    private Boolean available;

    private Artist artist;

    private Page<Track> tracks;

}
