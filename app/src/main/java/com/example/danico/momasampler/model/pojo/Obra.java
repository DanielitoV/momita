package com.example.danico.momasampler.model.pojo;

/**
 * Created by danico on 07/07/2018.
 */

public class Obra {
    private String image;
    private String name;
    private Integer artistID;

    public Obra(String image, String name, Integer artistID) {
        this.image = image;
        this.name = name;
        this.artistID = artistID;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Integer getArtistID() {
        return artistID;
    }
}
