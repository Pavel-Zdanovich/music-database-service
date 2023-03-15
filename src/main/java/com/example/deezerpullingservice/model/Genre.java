package com.example.deezerpullingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Genre {

    @Id
    private Integer id;

    @Column(nullable = false, length = 25)
    private String name;

//    private String picture;

//    private String pictureSmall;

//    private String pictureMedium;

//    private String pictureBig;

//    private String pictureXl;

}
