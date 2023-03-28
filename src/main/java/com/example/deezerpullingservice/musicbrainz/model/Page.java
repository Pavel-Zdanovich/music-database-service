package com.example.deezerpullingservice.musicbrainz.model;

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
            case "com.example.deezerpullingservice.musicbrainz.model.Artist" -> artists;
            case "com.example.deezerpullingservice.musicbrainz.model.Release" -> releases;
            default -> throw new RuntimeException("Unknown class: " + tClass.getName());
        };
    }
}
