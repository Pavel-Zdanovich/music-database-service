package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.deezer.model.Page;
import com.example.deezerpullingservice.model.Album;
import com.example.deezerpullingservice.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class AlbumConverter implements Converter<com.example.deezerpullingservice.deezer.model.Album, Album> {

    private final Converter<com.example.deezerpullingservice.deezer.model.Genre, Genre> genreConverter;

    @Override
    public Album convert(com.example.deezerpullingservice.deezer.model.Album source) {
        Album album = new Album();
        album.setId(source.getId());
        album.setTitle(source.getTitle());
        album.setReleaseDate(source.getReleaseDate());
        Page<com.example.deezerpullingservice.deezer.model.Genre> page = source.getGenres();
        if (page == null || page.getData() == null || page.getData().isEmpty()) {
            return album;
        }
        com.example.deezerpullingservice.deezer.model.Genre genre = page.getData()
                .stream()
                .filter(g -> g.getId().equals(source.getGenreId()))
                .findAny()
                .orElse(null);
        album.setGenre(genre == null ? null : genreConverter.convert(genre));
        return album;
    }
}
