package com.github.kiulian.downloader.model.videos.formats;

import com.github.kiulian.downloader.model.Extension;
import com.google.gson.JsonObject;

public abstract class Format {

    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String AUDIO_VIDEO = "audio/video";

    private final boolean isAdaptive;

    protected final Itag itag;
    protected final String url;
    protected final String mimeType;
    protected final Extension extension;
    protected final Integer bitrate;
    protected final Long contentLength;
    protected final Long lastModified;
    protected final Long approxDurationMs;
    protected final String clientVersion;
    protected Range initRange;
    protected Range indexRange;

    protected Format(JsonObject json, boolean isAdaptive, String clientVersion) {
        this.isAdaptive = isAdaptive;
        this.clientVersion = clientVersion;

        Itag itag;
        try {
            itag = Itag.valueOf("i" + json.get("itag").getAsInt());
        } catch (IllegalArgumentException e) {
            itag = Itag.unknown;
            itag.setId(json.get("itag").getAsInt());
        }
        this.itag = itag;

        url = json.get("url").getAsString().replace("\\u0026", "&");
        mimeType = json.get("mimeType").getAsString();
        bitrate = json.get("bitrate").getAsInt();
        contentLength = json.get("contentLength").getAsLong();
        lastModified = json.get("lastModified").getAsLong();
        approxDurationMs = json.get("approxDurationMs").getAsLong();

        JsonObject range = json.getAsJsonObject("initRange");
        if (range != null) initRange = new Range(range.get("start").getAsLong(), range.get("end").getAsLong());
        range = json.getAsJsonObject("indexRange");
        if (range != null) indexRange = new Range(range.get("start").getAsLong(), range.get("end").getAsLong());

        if (mimeType == null || mimeType.isEmpty()) {
            extension = Extension.UNKNOWN;
        } else if (mimeType.contains(Extension.MPEG4.value())) {
            if (this instanceof AudioFormat) extension = Extension.M4A;
            else extension = Extension.MPEG4;
        } else if (mimeType.contains(Extension.WEBM.value())) {
            if (this instanceof AudioFormat) extension = Extension.WEBA;
            else extension = Extension.WEBM;
        } else if (mimeType.contains(Extension.FLV.value())) {
            extension = Extension.FLV;
        } else if (mimeType.contains(Extension._3GP.value())) {
            extension = Extension._3GP;
        } else {
            extension = Extension.UNKNOWN;
        }
    }

    public abstract String type();

    public boolean isAdaptive() {
        return isAdaptive;
    }

    public String clientVersion() {
        return clientVersion;
    }

    public Itag itag() {
        return itag;
    }

    public Integer bitrate() {
        return bitrate;
    }

    public String mimeType() {
        return mimeType;
    }

    public String url() {
        return url;
    }

    public Long contentLength() {
        return contentLength;
    }

    public long lastModified() {
        return lastModified;
    }

    public Long duration() {
        return approxDurationMs;
    }

    public Extension extension() {
        return extension;
    }

    public Range initRange() {
        return initRange;
    }

    public Range indexRange() {
        return indexRange;
    }

    public static class Range {

        private final long start;
        private final long end;

        public Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }
    }
}
