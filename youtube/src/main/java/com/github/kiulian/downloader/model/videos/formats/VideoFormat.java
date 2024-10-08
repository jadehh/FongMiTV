package com.github.kiulian.downloader.model.videos.formats;

import com.github.kiulian.downloader.model.videos.quality.VideoQuality;
import com.google.gson.JsonObject;

public class VideoFormat extends Format {

    private final int fps;
    private final String qualityLabel;
    private final Integer width;
    private final Integer height;
    private VideoQuality videoQuality;

    public VideoFormat(JsonObject json, boolean isAdaptive, String clientVersion) {
        super(json, isAdaptive, clientVersion);
        fps = json.get("fps").getAsInt();
        qualityLabel = json.get("qualityLabel").getAsString();
        if (json.has("size")) {
            String[] split = json.get("size").getAsString().split("x");
            width = Integer.parseInt(split[0]);
            height = Integer.parseInt(split[1]);
        } else {
            width = json.get("width").getAsInt();
            height = json.get("height").getAsInt();
        }
        if (json.has("quality")) {
            try {
                videoQuality = VideoQuality.valueOf(json.get("quality").getAsString());
            } catch (IllegalArgumentException ignore) {
            }
        }
    }

    @Override
    public String type() {
        return VIDEO;
    }

    public int fps() {
        return fps;
    }

    public VideoQuality videoQuality() {
        return videoQuality != null ? videoQuality : itag.videoQuality();
    }

    public String qualityLabel() {
        return qualityLabel;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }
}
