package com.example.musicdatabaseservice.converter;

import com.example.musicdatabaseservice.model.Album;
import com.example.musicdatabaseservice.model.Artist;
import com.example.musicdatabaseservice.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class TrackConverter implements Converter<com.example.musicdatabaseservice.deezer.model.Track, Track> {

    private final Converter<com.example.musicdatabaseservice.deezer.model.Album, Album> albumConverter;

    private final Converter<com.example.musicdatabaseservice.deezer.model.Artist, Artist> artistConverter;

    @Override
    public Track convert(com.example.musicdatabaseservice.deezer.model.Track source) {
        Track track = new Track();
        track.setId(source.getId());
        track.setIsReadable(source.getReadable());
        track.setTitle(source.getTitle());
        track.setRank(source.getRank());
        track.setPreview(source.getPreview());
        com.example.musicdatabaseservice.deezer.model.Artist artist = source.getArtist();
        track.setArtist(artist == null ? null : artistConverter.convert(artist));
        com.example.musicdatabaseservice.deezer.model.Album album = source.getAlbum();
        track.setAlbum(album == null ? null : albumConverter.convert(album));
        return track;
    }
}
