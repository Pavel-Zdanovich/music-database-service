package com.example.deezerpullingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Artist {

    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

//    private String link;

//    private String share;

//    private String picture;

//    private String pictureSmall;

//    private String pictureMedium;

//    private String pictureBig;

//    private String pictureXl;

//    private Integer nbAlbum;

//    private Integer nbFan;

//    private Boolean radio;

//    private String tracklist;

}
