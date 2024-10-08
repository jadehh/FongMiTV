package com.github.kiulian.downloader.model.playlist;

public class PlaylistDetails {

    private String playlistId;
    private String title;

    public PlaylistDetails(String playlistId, String title) {
        this.playlistId = playlistId;
        this.title = title;
    }

    public String playlistId() {
        return playlistId;
    }

    public String title() {
        return title;
    }
}