package com.example.musicdatabaseservice;

import com.example.musicdatabaseservice.converter.AlbumConverter;
import com.example.musicdatabaseservice.converter.ArtistConverter;
import com.example.musicdatabaseservice.converter.GenreConverter;
import com.example.musicdatabaseservice.converter.TrackConverter;
import com.example.musicdatabaseservice.deezer.DeezerService;
import com.example.musicdatabaseservice.deezer.model.*;
import com.example.musicdatabaseservice.musicbrainz.MusicBrainzService;
import com.example.musicdatabaseservice.musicbrainz.model.Release;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class MusicDatabaseServiceApplicationTests {

    @Autowired
    AlbumConverter albumConverter;

    @Autowired
    ArtistConverter artistConverter;

    @Autowired
    GenreConverter genreConverter;

    @Autowired
    TrackConverter trackConverter;

    @Autowired
    DeezerService deezerService;

    @Autowired
    MusicBrainzService musicBrainzService;

    @Test
    void albumConvert() {
        Album source = new Album();
        source.setId(1);
        source.setTitle("Title");
        source.setGenreId(1);
        source.setGenres(new Page<>(List.of(new Genre(1, "Name"))));
        source.setReleaseDate(Date.from(LocalDate.of(2023, 3, 4).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        com.example.musicdatabaseservice.model.Album album = albumConverter.convert(source);

        Assertions.assertThat(album).hasNoNullFieldsOrProperties();
    }

    @Test
    void artistConvert() {
        Artist source = new Artist();
        source.setId(1);
        source.setName("Name");

        com.example.musicdatabaseservice.model.Artist artist = artistConverter.convert(source);

        Assertions.assertThat(artist).hasNoNullFieldsOrPropertiesExcept("country");
    }

    @Test
    void genreConvert() {
        Genre source = new Genre();
        source.setId(1);
        source.setName("Name");

        com.example.musicdatabaseservice.model.Genre genre = genreConverter.convert(source);

        Assertions.assertThat(genre).hasNoNullFieldsOrProperties();
    }

    @Test
    void trackConvert() {
        Track source = new Track();
        source.setId(1);
        source.setReadable(true);
        source.setTitle("Title");
        source.setRank(1);
        source.setPreview("Preview");

        com.example.musicdatabaseservice.model.Track track = trackConverter.convert(source);

        Assertions.assertThat(track).hasNoNullFieldsOrPropertiesExcept("artist", "album");
    }

    @Test
    void deezerService() throws ExecutionException, InterruptedException {
        CompletableFuture<Album> albumCompletableFuture = deezerService.getAsync(Album.class, 302127);
        Album album = albumCompletableFuture.get();
        Assertions.assertThat(album).hasNoNullFieldsOrProperties();

        CompletableFuture<Artist> artistCompletableFuture = deezerService.getAsync(Artist.class, 27);
        Artist artist = artistCompletableFuture.get();
        Assertions.assertThat(artist).hasNoNullFieldsOrProperties();

        CompletableFuture<Genre> genreCompletableFuture = deezerService.getAsync(Genre.class, 113);
        Genre genre = genreCompletableFuture.get();
        Assertions.assertThat(genre).hasNoNullFieldsOrProperties();

        CompletableFuture<Track> trackCompletableFuture = deezerService.getAsync(Track.class, 3135556);
        Track track = trackCompletableFuture.get();
        Assertions.assertThat(track).hasNoNullFieldsOrProperties();
    }

    @Test
    void musicBrainzService() throws ExecutionException, InterruptedException {
        CompletableFuture<com.example.musicdatabaseservice.musicbrainz.model.Artist> artistCompletableFuture =
                musicBrainzService.getAsync(com.example.musicdatabaseservice.musicbrainz.model.Artist.class, "Bring Me the Horizon");
        com.example.musicdatabaseservice.musicbrainz.model.Artist artist = artistCompletableFuture.get();
        Assertions.assertThat(artist).hasNoNullFieldsOrPropertiesExcept();

        CompletableFuture<Release> releaseCompletableFuture = musicBrainzService.getAsync(Release.class, "Can You Feel My Heart");
        Release release = releaseCompletableFuture.get();
        Assertions.assertThat(release).hasNoNullFieldsOrProperties();
    }

}
