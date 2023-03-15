package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.model.Album;
import com.example.deezerpullingservice.model.Artist;
import com.example.deezerpullingservice.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackConverter implements Converter<com.example.deezerpullingservice.deezer.model.Track, Track> {

    private final ConversionService conversionService;

    @Override
    public Track convert(com.example.deezerpullingservice.deezer.model.Track source) {
        Track track = new Track();
        track.setId(source.getId());
        track.setIsReadable(source.getReadable());
        track.setTitle(source.getTitle());
        track.setRank(source.getRank());
        track.setPreview(source.getPreview());
        com.example.deezerpullingservice.deezer.model.Artist artist = source.getArtist();
        track.setArtist(conversionService.convert(artist, Artist.class));
        com.example.deezerpullingservice.deezer.model.Album album = source.getAlbum();
        track.setAlbum(conversionService.convert(album, Album.class));
        return track;
    }
}
