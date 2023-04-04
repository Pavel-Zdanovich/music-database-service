package com.example.musicdatabaseservice.converter;

import com.example.musicdatabaseservice.model.Artist;
import org.springframework.core.convert.converter.Converter;

public class ArtistConverter implements Converter<com.example.musicdatabaseservice.deezer.model.Artist, Artist> {
    @Override
    public Artist convert(com.example.musicdatabaseservice.deezer.model.Artist source) {
        Artist artist = new Artist();
        artist.setId(source.getId());
        artist.setName(source.getName());
        return artist;
    }
}
