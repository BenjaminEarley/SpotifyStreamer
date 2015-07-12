package com.benjaminearley.spotifystreamer.model;

import java.io.Serializable;

public class TrackShort implements Serializable {

    private String albumName;
    private String songName;
    private String albumUrl;

    public TrackShort(String albumName, String songName, String albumUrl) {
        this.albumName = albumName;
        this.songName = songName;
        this.albumUrl = albumUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSongName() {
        return songName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }
}
