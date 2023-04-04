package com.example.musicdatabaseservice.musicbrainz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Release {

    private UUID id;

    private Integer score;

    private String title;

    private String status;

    @SerializedName("text-representation")
    private Map<String, String> textRepresentation;

    @SerializedName("artist-credit")
    private List<Map<String, Object>> artistCredit;

    private Date date;

    private String country;

    public String getTextRepresentation() {
        return textRepresentation.get("language");
    }

    public Artist getArtistCredit() {
        Map<String, String> map = (Map<String, String>) artistCredit.get(0).get("artist");
        Artist artist = new Artist();
        artist.setId(UUID.fromString(map.get("id")));
        artist.setName(map.get("name"));
        return artist;
    }
}
