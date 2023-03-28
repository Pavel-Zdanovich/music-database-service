package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.model.Artist;
import org.springframework.core.convert.converter.Converter;

public class ArtistConverter implements Converter<com.example.deezerpullingservice.deezer.model.Artist, Artist> {
    @Override
    public Artist convert(com.example.deezerpullingservice.deezer.model.Artist source) {
        Artist artist = new Artist();
        artist.setId(source.getId());
        artist.setName(source.getName());
        return artist;
    }
}
