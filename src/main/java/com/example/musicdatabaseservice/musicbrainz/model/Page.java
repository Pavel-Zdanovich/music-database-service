package com.example.musicdatabaseservice.musicbrainz.model;

import java.util.Date;
import java.util.List;

public class Page {

    private Date created;

    private Integer count;

    private Integer offset;

    private List<Artist> artists;

    private List<Release> releases;

    public <T> Object get(Class<T> tClass) {
        return switch (tClass.getName()) {
            case "com.example.musicdatabaseservice.musicbrainz.model.Artist" -> artists;
            case "com.example.musicdatabaseservice.musicbrainz.model.Release" -> releases;
            default -> throw new RuntimeException("Unknown class: " + tClass.getName());
        };
    }
}
