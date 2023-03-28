package com.example.deezerpullingservice.repository;

import com.example.deezerpullingservice.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Page<Artist> findByCountryIsNull(Pageable pageable);

}
