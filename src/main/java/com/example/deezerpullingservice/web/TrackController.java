package com.example.deezerpullingservice.web;

import com.example.deezerpullingservice.model.Track;
import com.example.deezerpullingservice.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;

    @GetMapping
    public Page<Track> read(@SortDefault Pageable pageable) {
        return trackService.findAll(pageable);
    }

    @GetMapping("/album")
    public List<Track> readByAlbum(@RequestParam Integer id) {
        return trackService.findByAlbum(id);
    }

    @GetMapping("/artist")
    public Page<Track> readByArtist(@RequestParam Integer id, @SortDefault Pageable pageable) {
        return trackService.findByArtist(id, pageable);
    }

    @GetMapping("/country")
    public Page<Track> readByCountries(@RequestParam Set<String> code, @SortDefault Pageable pageable) {
        return trackService.findByCountries(code, pageable);
    }

    @GetMapping("/language")
    public Page<Track> readByLanguage(@RequestParam String code, @SortDefault Pageable pageable) {
        return trackService.findByLanguage(code, pageable);
    }

    @GetMapping("/genre")
    public Page<Track> readByGenre(@RequestParam String name, @SortDefault Pageable pageable) {
        return trackService.findByGenre(name, pageable);
    }
}
