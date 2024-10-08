package com.github.kiulian.downloader.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static String parseRuns(JsonObject container) {
        if (container == null) {
            return null;
        }
        JsonArray runs = container.getAsJsonArray("runs");
        if (runs == null) {
            return null;
        } else if (runs.size() == 1) {
            return runs.get(0).getAsJsonObject().get("text").getAsString();
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < runs.size(); i++) {
                builder.append(runs.get(i).getAsJsonObject().get("text").getAsString());
            }
            return builder.toString();
        }
    }

    public static List<String> parseThumbnails(JsonObject container) {
        if (container == null) {
            return null;
        }
        JsonArray jsonThumbnails = container.getAsJsonArray("thumbnails");
        if (jsonThumbnails == null) {
            return null;
        } else {
            List<String> thumbnails = new ArrayList<>(jsonThumbnails.size());
            for (int i = 0; i < jsonThumbnails.size(); i++) {
                JsonObject jsonThumbnail = jsonThumbnails.get(i).getAsJsonObject();
                if (jsonThumbnail.has("url")) {
                    thumbnails.add(jsonThumbnail.get("url").getAsString());
                }
            }
            return thumbnails;
        }
    }
}
