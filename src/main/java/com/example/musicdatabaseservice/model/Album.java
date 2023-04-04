package com.example.musicdatabaseservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Album {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String title;

    private Date releaseDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Genre genre;

}
