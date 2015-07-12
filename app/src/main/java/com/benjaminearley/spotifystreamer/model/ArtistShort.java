package com.benjaminearley.spotifystreamer.model;

import java.io.Serializable;

public class ArtistShort implements Serializable {

    private String albumUrl;
    private String name;

    public ArtistShort(String name, String albumUrl) {
        this.name = name;
        this.albumUrl = albumUrl;
    }


    public String getAlbumUrl() {
        return albumUrl;
    }

    public String getName() {
        return name;
    }
}
