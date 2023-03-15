package com.example.deezerpullingservice;

import com.example.deezerpullingservice.model.*;
import com.example.deezerpullingservice.repository.ArtistRepository;
import com.example.deezerpullingservice.repository.TrackRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    TrackRepository trackRepository;

    static Album album;

    static Artist artist;

    static Country country;

    static Genre genre;

    static Language language;

    static Track track;

    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
            .withEqualsForType((l1, l2) -> l1.getCode().equals(l2.getCode()), Language.class)
            .build();

    static Album album() {
        Album album = new Album();
        album.setId(1);
        album.setTitle("Album 1");
        album.setReleaseDate(Timestamp.valueOf("1998-08-31 00:00:00"));
        return album;
    }

    static Artist artist() {
        Artist artist = new Artist();
        artist.setId(1);
        artist.setName("Artist 1");
        return artist;
    }

    static Country country() {
        Country country = new Country();
        country.setCode("UA");
        country.setName("Ukraine");
        return country;
    }

    static Genre genre() {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");
        return genre;
    }

    static Language language() {
        Language language = new Language();
        language.setCode("uk");
        return language;
    }

    static Track track() {
        Track track = new Track();
        track.setId(1);
        track.setIsReadable(true);
        track.setTitle("Track 1");
        track.setRank(10);
        track.setPreview("Preview");
        return track;
    }

    @BeforeAll
    static void before() {
        album = album();
        artist = artist();
        country = country();
        genre = genre();
        language = language();
        track = track();

        album.setGenre(genre);
        artist.setCountry(country);
        country.addLanguage(language);
        track.setAlbum(album);
        track.setArtist(artist);
    }

    @Test
    void getByAlbumId() {
        trackRepository.saveAndFlush(track);
        List<Track> actual = trackRepository.getByAlbumId(album.getId());
        Assertions.assertThat(actual)
                .hasSize(1)
                .first().usingRecursiveComparison(configuration).isEqualTo(track);
    }

    @Test
    void getByArtistId() {
        trackRepository.saveAndFlush(track);
        Page<Track> page = trackRepository.getByArtistId(artist.getId(), Pageable.unpaged());
        Assertions.assertThat(page)
                .hasSize(1)
                .first().usingRecursiveComparison(configuration).isEqualTo(track);
    }

    @Test
    void getByCountries() {
        trackRepository.saveAndFlush(track);
        Page<Track> page = trackRepository.getByCountries(Set.of("UA"), Pageable.unpaged());
        Assertions.assertThat(page)
                .hasSize(1)
                .first().usingRecursiveComparison(configuration).isEqualTo(track);
    }

    @Test
    void getByGenre() {
        trackRepository.saveAndFlush(track);
        Page<Track> page = trackRepository.getByGenre("Genre 1", Pageable.unpaged());
        Assertions.assertThat(page)
                .hasSize(1)
                .first().usingRecursiveComparison(configuration).isEqualTo(track);
    }

    @Test
    void getByLanguage() {
        trackRepository.saveAndFlush(track);
        Page<Track> page = trackRepository.getByLanguage("uk", Pageable.unpaged());
        Assertions.assertThat(page)
                .hasSize(1)
                .first().usingRecursiveComparison(configuration).isEqualTo(track);
    }
}
