package com.example.deezerpullingservice.deezer.model;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private List<T> data;

}
