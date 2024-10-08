package com.github.kiulian.downloader.model.videos;

import com.github.kiulian.downloader.model.AbstractVideoDetails;
import com.google.gson.JsonObject;

public class VideoDetails extends AbstractVideoDetails {

    private String liveUrl;
    private boolean isLiveContent;

    public VideoDetails(String videoId) {
        this.videoId = videoId;
    }

    public VideoDetails(JsonObject json, String liveHLSUrl) {
        super(json);
        title = json.get("title").getAsString();
        author = json.get("author").getAsString();
        isLive = json.has("isLive") && json.get("isLive").getAsBoolean();
        isLiveContent = json.has("isLiveContent") && json.get("isLiveContent").getAsBoolean();
        liveUrl = liveHLSUrl;
    }

    @Override
    public boolean isDownloadable() {
        return !isLive() && !(isLiveContent && lengthSeconds() == 0);
    }

    public String liveUrl() {
        return liveUrl;
    }
}
