package com.example.deezerpullingservice.service;

import com.example.deezerpullingservice.model.Track;
import com.example.deezerpullingservice.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TrackService {

    private final TrackRepository repository;

    public <S extends Track> S save(S entity) {
        return repository.saveAndFlush(entity);
    }

    public Page<Track> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Track> findByAlbum(Integer albumId) {
        return repository.getByAlbumId(albumId);
    }

    public Page<Track> findByArtist(Integer artistId, Pageable pageable) {
        return repository.getByArtistId(artistId, pageable);
    }

    public Page<Track> findByCountries(Set<String> code, Pageable pageable) {
        return repository.getByCountries(code, pageable);
    }

    public Page<Track> findByLanguage(String code, Pageable pageable) {
        return repository.getByLanguage(code, pageable);
    }

    public Page<Track> findByGenre(String name, Pageable pageable) {
        return repository.getByGenre(name, pageable);
    }
}
