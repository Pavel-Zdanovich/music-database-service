package com.example.musicdatabaseservice.converter;

import com.example.musicdatabaseservice.model.Genre;
import org.springframework.core.convert.converter.Converter;

public class GenreConverter implements Converter<com.example.musicdatabaseservice.deezer.model.Genre, Genre> {
    @Override
    public Genre convert(com.example.musicdatabaseservice.deezer.model.Genre source) {
        Genre genre = new Genre();
        genre.setId(source.getId());
        genre.setName(source.getName());
        return genre;
    }
}
