package com.github.kiulian.downloader.model.videos.formats;

import com.github.kiulian.downloader.model.videos.quality.AudioQuality;
import com.google.gson.JsonObject;

public class AudioFormat extends Format {

    private final Integer averageBitrate;
    private final Integer audioSampleRate;
    private AudioQuality audioQuality;

    public AudioFormat(JsonObject json, boolean isAdaptive, String clientVersion) {
        super(json, isAdaptive, clientVersion);
        audioSampleRate = json.get("audioSampleRate").getAsInt();
        averageBitrate = json.get("averageBitrate").getAsInt();
        if (json.has("audioQuality")) {
            String[] split = json.get("audioQuality").getAsString().split("_");
            String quality = split[split.length - 1].toLowerCase();
            try {
                audioQuality = AudioQuality.valueOf(quality);
            } catch (IllegalArgumentException ignore) {
            }
        }
    }

    @Override
    public String type() {
        return AUDIO;
    }

    public Integer averageBitrate() {
        return averageBitrate;
    }

    public AudioQuality audioQuality() {
        return audioQuality != null ? audioQuality : itag.audioQuality();
    }

    public Integer audioSampleRate() {
        return audioSampleRate;
    }
}
