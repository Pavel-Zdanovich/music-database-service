package com.example.deezerpullingservice.converter;

import com.example.deezerpullingservice.model.Album;
import com.example.deezerpullingservice.model.Artist;
import com.example.deezerpullingservice.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class TrackConverter implements Converter<com.example.deezerpullingservice.deezer.model.Track, Track> {

    private final Converter<com.example.deezerpullingservice.deezer.model.Album, Album> albumConverter;

    private final Converter<com.example.deezerpullingservice.deezer.model.Artist, Artist> artistConverter;

    @Override
    public Track convert(com.example.deezerpullingservice.deezer.model.Track source) {
        Track track = new Track();
        track.setId(source.getId());
        track.setIsReadable(source.getReadable());
        track.setTitle(source.getTitle());
        track.setRank(source.getRank());
        track.setPreview(source.getPreview());
        com.example.deezerpullingservice.deezer.model.Artist artist = source.getArtist();
        track.setArtist(artist == null ? null : artistConverter.convert(artist));
        com.example.deezerpullingservice.deezer.model.Album album = source.getAlbum();
        track.setAlbum(album == null ? null : albumConverter.convert(album));
        return track;
    }
}
