package com.example.musicdatabaseservice.musicbrainz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.UUID;

@Data
public class Artist {

    private UUID id;

    private String type;

    @SerializedName("type-id")
    private UUID typeId;

    private Integer score;

    private String name;

    private String country;

}
