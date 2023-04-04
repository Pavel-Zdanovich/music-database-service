package com.example.musicdatabaseservice;

import com.example.musicdatabaseservice.model.*;
import com.example.musicdatabaseservice.service.AlbumService;
import com.example.musicdatabaseservice.service.ArtistService;
import com.example.musicdatabaseservice.service.TrackService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.util.List;

@WebMvcTest
public class WebTest {

    @MockBean
    AlbumService albumService;

    @MockBean
    ArtistService artistService;

    @MockBean
    TrackService trackService;

    @Autowired
    MockMvc mockMvc;

    Track track() {
        Track track = new Track();
        track.setId(1);
        track.setIsReadable(true);
        track.setTitle("Title");
        track.setRank(1);
        track.setPreview("Preview");
        Artist artist = new Artist();
        artist.setId(1);
        artist.setName("Name");
        Country country = new Country();
        country.setCode("Code");
        country.setName("Name");
        artist.setCountry(country);
        track.setArtist(artist);
        Album album = new Album();
        album.setId(1);
        album.setTitle("Title");
        album.setReleaseDate(Date.valueOf("2023-04-04"));
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Name");
        album.setGenre(genre);
        track.setAlbum(album);
        return track;
    }

    @Test
    void read() throws Exception {
        List<Track> list = List.of(track());
        Page<Track> page = new PageImpl<>(list);

        Mockito.when(trackService.findAll(Mockito.any())).thenReturn(page);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void readByAlbum() throws Exception {
        List<Track> list = List.of(track());

        Mockito.when(trackService.findByAlbum(Mockito.any())).thenReturn(list);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track/album")
                                .param("id", "1")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void readByArtist() throws Exception {
        List<Track> list = List.of(track());
        Page<Track> page = new PageImpl<>(list);

        Mockito.when(trackService.findByArtist(Mockito.any(), Mockito.any())).thenReturn(page);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track/artist")
                                .param("id", "1")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void readByCountry() throws Exception {
        List<Track> list = List.of(track());
        Page<Track> page = new PageImpl<>(list);

        Mockito.when(trackService.findByCountries(Mockito.any(), Mockito.any())).thenReturn(page);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track/country")
                                .param("code", "BY")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void readByGenre() throws Exception {
        List<Track> list = List.of(track());
        Page<Track> page = new PageImpl<>(list);

        Mockito.when(trackService.findByGenre(Mockito.any(), Mockito.any())).thenReturn(page);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track/genre")
                                .param("name", "Name")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void readByLanguage() throws Exception {
        List<Track> list = List.of(track());
        Page<Track> page = new PageImpl<>(list);

        Mockito.when(trackService.findByLanguage(Mockito.any(), Mockito.any())).thenReturn(page);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/track/language")
                                .param("code", "be")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }

    @Test
    void deleteById() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete("/track")
                                .param("id", "1")
                ).andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                );
    }
}
