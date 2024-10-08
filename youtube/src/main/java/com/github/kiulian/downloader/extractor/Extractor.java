package com.github.kiulian.downloader.extractor;

import com.github.kiulian.downloader.YoutubeException;
import com.google.gson.JsonObject;

public interface Extractor {

    JsonObject extractInitialDataFromHtml(String html) throws YoutubeException;

    JsonObject extractPlayerConfigFromHtml(String html) throws YoutubeException;

    String extractJsUrlFromConfig(JsonObject config, String videoId) throws YoutubeException;

    String extractClientVersionFromContext(JsonObject context);

    int extractIntegerFromText(String text);
}
