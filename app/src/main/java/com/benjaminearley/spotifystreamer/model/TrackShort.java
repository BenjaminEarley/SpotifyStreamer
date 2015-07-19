package com.benjaminearley.spotifystreamer.model;

import java.io.Serializable;

public class TrackShort implements Serializable {

    private String albumName;
    private String songName;
    private String albumUrl;
    private String songUrl;

    public TrackShort(String albumName, String songName, String albumUrl, String songUrl) {
        this.albumName = albumName;
        this.songName = songName;
        this.albumUrl = albumUrl;
        this.songUrl = songUrl;
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

    public String getSongUrl() {
        return songUrl;
    }
}
