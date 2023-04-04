package com.example.musicdatabaseservice.service;

import com.example.musicdatabaseservice.model.Artist;
import com.example.musicdatabaseservice.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ArtistService {

    private final ArtistRepository artistRepository;

    public <S extends Artist> S save(S entity) {
        return artistRepository.saveAndFlush(entity);
    }

    public Page<Artist> findByCountryIsNull(Pageable pageable) {
        return artistRepository.findByCountryIsNull(pageable);
    }
}
