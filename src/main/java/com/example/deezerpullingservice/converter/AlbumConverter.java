package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.model.Album;
import com.example.deezerpullingservice.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumConverter implements Converter<com.example.deezerpullingservice.deezer.model.Album, Album> {

    private final ConversionService conversionService;

    @Override
    public Album convert(com.example.deezerpullingservice.deezer.model.Album source) {
        Album album = new Album();
        album.setId(source.getId());
        album.setTitle(source.getTitle());
        album.setReleaseDate(source.getReleaseDate());
        com.example.deezerpullingservice.deezer.model.Genre genre = source.getGenres().getData()
                .stream()
                .filter(g -> g.getId().equals(source.getGenreId()))
                .findAny()
                .orElseThrow();
        album.setGenre(conversionService.convert(genre, Genre.class));
        return album;
    }
}
