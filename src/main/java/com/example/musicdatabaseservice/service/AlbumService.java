package com.example.musicdatabaseservice.service;

import com.example.musicdatabaseservice.model.Album;
import com.example.musicdatabaseservice.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AlbumService {

    private final AlbumRepository albumRepository;

    public <S extends Album> S save(S entity) {
        return albumRepository.saveAndFlush(entity);
    }

    public Page<Album> findByGenreIsNull(Pageable pageable) {
        return albumRepository.findByGenreIsNull(pageable);
    }
}

