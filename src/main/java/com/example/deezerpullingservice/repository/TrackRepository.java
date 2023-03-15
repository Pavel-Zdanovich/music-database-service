package com.example.deezerpullingservice.repository;

import com.example.deezerpullingservice.model.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

    List<Track> getByAlbumId(Integer albumId);

    Page<Track> getByArtistId(Integer artistId, Pageable pageable);

    @Query(
            value = "SELECT t.* FROM track t " +
                    "LEFT JOIN artist a ON t.artist_id = a.id " +
                    "WHERE a.country_code IN ?1",
            nativeQuery = true
    )
    Page<Track> getByCountries(Set<String> code, Pageable pageable);

    @Query(
            value = "SELECT t.* FROM track t " +
                    "LEFT JOIN album a ON t.album_id = a.id " +
                    "LEFT JOIN genre g ON a.genre_id = g.id " +
                    "WHERE g.name = ?1",
            nativeQuery = true
    )
    Page<Track> getByGenre(String name, Pageable pageable);

    @Query(
            value = "SELECT t.* FROM track t " +
                    "LEFT JOIN artist a ON t.artist_id = a.id " +
                    "LEFT JOIN country_language cl ON a.country_code = cl.country_code " +
                    "WHERE cl.language_code = ?1",
            nativeQuery = true
    )
    Page<Track> getByLanguage(String code, Pageable pageable);

}
