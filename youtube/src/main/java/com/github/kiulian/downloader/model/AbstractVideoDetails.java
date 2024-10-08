package com.github.kiulian.downloader.model;

import com.google.gson.JsonObject;

import java.util.List;

public abstract class AbstractVideoDetails {

    protected String videoId;
    private List<String> thumbnails;

    // Subclass specific extraction
    protected int lengthSeconds;
    protected String title;
    protected String author;
    protected boolean isLive;

    protected boolean isDownloadable() {
        return (!isLive() && lengthSeconds() != 0);
    }

    public AbstractVideoDetails() {
    }

    public AbstractVideoDetails(JsonObject json) {
        videoId = json.get("videoId").getAsString();
        thumbnails = Utils.parseThumbnails(json.getAsJsonObject("thumbnail"));
        if (json.has("lengthSeconds")) lengthSeconds = json.get("lengthSeconds").getAsInt();
    }

    public String videoId() {
        return videoId;
    }

    public String title() {
        return title;
    }

    public int lengthSeconds() {
        return lengthSeconds;
    }

    public List<String> thumbnails() {
        return thumbnails;
    }

    public boolean isLive() {
        return isLive;
    }
}
