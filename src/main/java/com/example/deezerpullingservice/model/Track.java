package com.example.deezerpullingservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Track {

    @Id
    private Integer id;

    private Boolean isReadable;

    @Column(nullable = false)
    private String title;

    @Column(name = "rating")
    private Integer rank;

    private String preview;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Artist artist;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Album album;

}
