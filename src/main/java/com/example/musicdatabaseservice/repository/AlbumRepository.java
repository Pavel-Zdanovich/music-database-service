package com.example.musicdatabaseservice.repository;

import com.example.musicdatabaseservice.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    Page<Album> findByGenreIsNull(Pageable pageable);

}
