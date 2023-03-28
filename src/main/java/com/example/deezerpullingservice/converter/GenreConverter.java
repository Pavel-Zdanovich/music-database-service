package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.model.Genre;
import org.springframework.core.convert.converter.Converter;

public class GenreConverter implements Converter<com.example.deezerpullingservice.deezer.model.Genre, Genre> {
    @Override
    public Genre convert(com.example.deezerpullingservice.deezer.model.Genre source) {
        Genre genre = new Genre();
        genre.setId(source.getId());
        genre.setName(source.getName());
        return genre;
    }
}
