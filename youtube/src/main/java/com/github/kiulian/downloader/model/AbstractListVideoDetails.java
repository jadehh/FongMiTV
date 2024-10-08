package com.github.kiulian.downloader.model;

import com.google.gson.JsonObject;

public class AbstractListVideoDetails extends AbstractVideoDetails {

    public AbstractListVideoDetails(JsonObject json) {
        super(json);
        author = Utils.parseRuns(json.getAsJsonObject("shortBylineText"));
        JsonObject jsonTitle = json.getAsJsonObject("title");
        if (jsonTitle.has("simpleText")) {
            title = jsonTitle.get("simpleText").getAsString();
        } else {
            title = Utils.parseRuns(jsonTitle);
        }
    }
}