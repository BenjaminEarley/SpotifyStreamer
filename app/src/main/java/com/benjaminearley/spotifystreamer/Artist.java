package com.benjaminearley.spotifystreamer;

/**
 * Created by bearley on 6/27/15.
 */
public class Artist {

    private String name;
    private int imageId;

    public Artist(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
