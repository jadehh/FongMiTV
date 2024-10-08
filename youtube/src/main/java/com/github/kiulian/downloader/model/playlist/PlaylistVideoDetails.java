package com.github.kiulian.downloader.model.playlist;

import com.github.kiulian.downloader.model.AbstractListVideoDetails;
import com.google.gson.JsonObject;

public class PlaylistVideoDetails extends AbstractListVideoDetails {

    private int index;
    private boolean isPlayable;

    public PlaylistVideoDetails(JsonObject json) {
        super(json);
        if (!thumbnails().isEmpty()) {
            // Otherwise, contains "/hqdefault.jpg?"
            isLive = thumbnails().get(0).contains("/hqdefault_live.jpg?");
        }
        if (json.has("index")) {
            index = json.getAsJsonObject("index").get("simpleText").getAsInt();
        }
        isPlayable = json.get("isPlayable").getAsBoolean();
    }

    @Override
    protected boolean isDownloadable() {
        return isPlayable && super.isDownloadable();
    }

    public int index() {
        return index;
    }

    public boolean isPlayable() {
        return isPlayable;
    }
}