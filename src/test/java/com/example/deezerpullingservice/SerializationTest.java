package com.example.deezerpullingservice;

import com.example.deezerpullingservice.config.Config;
import com.example.deezerpullingservice.deezer.model.Album;
import com.example.deezerpullingservice.deezer.model.Artist;
import com.example.deezerpullingservice.deezer.model.Genre;
import com.example.deezerpullingservice.deezer.model.Track;
import com.example.deezerpullingservice.musicbrainz.model.Page;
import com.example.deezerpullingservice.musicbrainz.model.Release;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@JsonTest
@Import(Config.class)
public class SerializationTest {

    @Autowired
    GsonTester<Album> deezerAlbumGsonTester;

    @Autowired
    GsonTester<Artist> deezerArtistGsonTester;

    @Autowired
    GsonTester<Genre> deezerGenreGsonTester;

    @Autowired
    GsonTester<Track> deezerTrackGsonTester;

    @Autowired
    GsonTester<Page<com.example.deezerpullingservice.musicbrainz.model.Artist>> musicbrainzArtistGsonTester;

    @Autowired
    GsonTester<Page<Release>> deezerReleaseGsonTester;

    @Test
    void deezerAlbum() throws IOException {
        ObjectContent<Album> albumObjectContent = deezerAlbumGsonTester.read("/deezer/album.json");
        Album album = albumObjectContent.getObject();
        Assertions.assertThat(album).hasNoNullFieldsOrProperties();
    }

    @Test
    void deezerArtist() throws IOException {
        ObjectContent<Artist> artistObjectContent = deezerArtistGsonTester.read("/deezer/artist.json");
        Artist artist = artistObjectContent.getObject();
        Assertions.assertThat(artist).hasNoNullFieldsOrProperties();
    }

    @Test
    void deezerGenre() throws IOException {
        ObjectContent<Genre> genreObjectContent = deezerGenreGsonTester.read("/deezer/genre.json");
        Genre genre = genreObjectContent.getObject();
        Assertions.assertThat(genre).hasNoNullFieldsOrProperties();
    }

    @Test
    void deezerTrack() throws IOException {
        ObjectContent<Track> trackObjectContent = deezerTrackGsonTester.read("/deezer/track.json");
        Track track = trackObjectContent.getObject();
        Assertions.assertThat(track).hasNoNullFieldsOrProperties();
    }

    @Test
    void musicbrainzArtist() throws IOException {
        ObjectContent<Page<com.example.deezerpullingservice.musicbrainz.model.Artist>> artistObjectContent
                = musicbrainzArtistGsonTester.read("/musicbrainz/artist.json");
        Page<com.example.deezerpullingservice.musicbrainz.model.Artist> page = artistObjectContent.getObject();
        com.example.deezerpullingservice.musicbrainz.model.Artist artist = page.get(com.example.deezerpullingservice.musicbrainz.model.Artist.class).get(0);
        Assertions.assertThat(artist).hasNoNullFieldsOrProperties();
    }

    @Test
    void musicbrainzRelease() throws IOException {
        ObjectContent<Page<Release>> releaseObjectContent = deezerReleaseGsonTester.read("/musicbrainz/release.json");
        Page<Release> page = releaseObjectContent.getObject();
        Release release = page.get(Release.class).get(0);
        Assertions.assertThat(release).hasNoNullFieldsOrProperties();
    }
}
