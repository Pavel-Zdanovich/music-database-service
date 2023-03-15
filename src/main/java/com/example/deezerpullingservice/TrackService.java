package com.example.deezerpullingservice;

import com.example.deezerpullingservice.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class TrackService {

    private final TrackRepository repository;

    public <S extends Track> S save(S entity) {
        return repository.saveAndFlush(entity);
    }

    public <S extends Track> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAllAndFlush(entities);
    }

    public Optional<Track> findById(Integer id) {
        return repository.findById(id);
    }

    public Page<Track> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
