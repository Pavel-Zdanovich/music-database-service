package com.example.deezerpullingservice.musicbrainz.model;

import java.util.Date;
import java.util.List;

public class Page<T> {

    private Date created;

    private Integer count;

    private Integer offset;

    private List<T> artists;

    private List<T> releases;

    public List<T> get(Class<T> tClass) {
        return switch (tClass.getName()) {
            case "com.example.deezerpullingservice.musicbrainz.model.Artist" -> artists;
            case "com.example.deezerpullingservice.musicbrainz.model.Release" -> releases;
            default -> throw new RuntimeException("Unknown class: " + tClass.getName());
        };
    }
}
