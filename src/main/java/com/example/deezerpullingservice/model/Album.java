package com.example.deezerpullingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Album {

    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

//    private String upc;

//    private String link;

//    private String share;

//    private String cover;

//    private String coverSmall;

//    private String coverMedium;

//    private String coverBig;

//    private String coverXl;

//    private String md5Image;

//    private Integer genreId;

//    private PageOf<Genre> genres;

//    @Column(length = 50)
//    private String label;

//    private Integer nbTracks;

//    private Integer duration;

//    private Integer fans;

//    private Integer rating;

    private Date releaseDate;

//    @Column(length = 25)
//    private String recordType;

//    private Boolean isAvailable;

//    private Album alternative;

//    private String tracklist;

//    private Boolean containsExplicitLyrics;

//    private Integer explicitContentLyrics;

//    private Integer explicitContentCover;

//    private List<Contributor> contributors;

/*
    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
*/

/*
    @Embedded
    @AssociationOverride(
            name = "data",
            joinColumns = @JoinColumn(name = "album_id")
    )
    private PageOf<Track> tracks;
*/

}
