package com.benjaminearley.spotifystreamer;

/**
 * Created by bearley on 6/27/15.
 */
public class Song {

    private String album;
    private String track;
    private int coverId;

    public Song(String album, String track, int coverId) {
        this.album = album;
        this.track = track;
        this.coverId = coverId;
    }

    public String getTrack() {
        return track;
    }

    public int getCoverId() {
        return coverId;
    }

    public String getAlbum() {
        return album;
    }

}
