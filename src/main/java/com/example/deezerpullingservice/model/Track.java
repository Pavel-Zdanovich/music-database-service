package com.example.deezerpullingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {

    @Id
    private Integer id;

//    private Boolean isReadable;

    @Column(nullable = false, length = 100)
    private String title;

//    private String titleShort;

//    private String titleVersion;

//    private Boolean isUnseen;

//    private String isrc;

//    private String link;

//    private String share;

//    private Integer duration;

//    private Integer trackPosition;

//    private Integer diskNumber;

//    private Integer rank;

//    private Date releaseDate;

//    private Boolean containsExplicitLyrics;

//    private Integer explicitContentLyrics;

//    private Integer explicitContentCover;

//    private String preview;

//    private Float bpm;

//    private Float gain;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "country_track",
            inverseJoinColumns = @JoinColumn(name = "country_code")
    )
    private Set<Country> availableCountries;

//    private Track alternative;

//    @OneToMany
//    private List<Artist> contributors;

//    private String md5Image;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private Album album;

//    private Integer position;

}
