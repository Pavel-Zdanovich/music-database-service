package com.example.musicdatabaseservice.repository;

import com.example.musicdatabaseservice.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Page<Artist> findByCountryIsNull(Pageable pageable);

}
