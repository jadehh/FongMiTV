package com.github.kiulian.downloader.extractor;

import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorImpl implements Extractor {

    private static final String DEFAULT_CLIENT_VERSION = "2.20200720.00.02";

    private static final List<Pattern> YT_PLAYER_CONFIG_PATTERNS = Arrays.asList(
            Pattern.compile(";ytplayer\\.config = (\\{.*?\\})\\;ytplayer"),
            Pattern.compile(";ytplayer\\.config = (\\{.*?\\})\\;"),
            Pattern.compile("ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\s*\\;")
    );

    private static final List<Pattern> YT_INITIAL_DATA_PATTERNS = Arrays.asList(
            Pattern.compile("window\\[\"ytInitialData\"\\] = (\\{.*?\\});"),
            Pattern.compile("ytInitialData = (\\{.*?\\});")
    );

    private static final Pattern TEXT_NUMBER_REGEX = Pattern.compile("[0-9]+[0-9, ']*");
    private static final Pattern ASSETS_JS_REGEX = Pattern.compile("\"assets\":.+?\"js\":\\s*\"([^\"]+)\"");
    private static final Pattern EMB_JS_REGEX = Pattern.compile("\"jsUrl\":\\s*\"([^\"]+)\"");

    private final Downloader downloader;

    public ExtractorImpl(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public JsonObject extractInitialDataFromHtml(String html) throws YoutubeException {
        String ytInitialData = null;
        for (Pattern pattern : YT_INITIAL_DATA_PATTERNS) {
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                ytInitialData = matcher.group(1);
            }
        }
        if (ytInitialData == null) {
            throw new YoutubeException.BadPageException("Could not find initial data on web page");
        }
        try {
            return JsonParser.parseString(ytInitialData).getAsJsonObject();
        } catch (Exception e) {
            throw new YoutubeException.BadPageException("Initial data contains invalid json");
        }
    }

    @Override
    public JsonObject extractPlayerConfigFromHtml(String html) throws YoutubeException {
        String ytPlayerConfig = null;
        for (Pattern pattern : YT_PLAYER_CONFIG_PATTERNS) {
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                ytPlayerConfig = matcher.group(1);
                break;
            }
        }
        if (ytPlayerConfig == null) {
            throw new YoutubeException.BadPageException("Could not find player config on web page");
        }
        try {
            JsonObject config = JsonParser.parseString(ytPlayerConfig).getAsJsonObject();
            if (config.has("args")) {
                return config;
            } else {
                JsonObject obj = new JsonObject();
                JsonObject args = new JsonObject();
                args.add("player_response", config);
                obj.add("args", args);
                return obj;
            }
        } catch (Exception e) {
            throw new YoutubeException.BadPageException("Player config contains invalid json");
        }
    }

    @Override
    public String extractJsUrlFromConfig(JsonObject config, String videoId) throws YoutubeException {
        String js = null;
        if (config.has("assets")) {
            js = config.getAsJsonObject("assets").get("js").getAsString();
        } else {
            // if assets not found - download embed webpage and search there
            Response<String> response = downloader.downloadWebpage(new RequestWebpage("https://www.youtube.com/embed/" + videoId));
            String html = response.data();
            Matcher matcher = ASSETS_JS_REGEX.matcher(html);
            if (matcher.find()) {
                js = matcher.group(1).replace("\\", "");
            } else {
                matcher = EMB_JS_REGEX.matcher(html);
                if (matcher.find()) {
                    js = matcher.group(1).replace("\\", "");
                }
            }
        }
        if (js == null) {
            throw new YoutubeException.BadPageException("Could not extract js url: assets not found");
        }
        return "https://youtube.com" + js;
    }

    @Override
    public String extractClientVersionFromContext(JsonObject context) {
        JsonArray trackingParams = context.getAsJsonArray("serviceTrackingParams");
        if (trackingParams == null) {
            return DEFAULT_CLIENT_VERSION;
        }
        for (int ti = 0; ti < trackingParams.size(); ti++) {
            JsonArray params = trackingParams.get(ti).getAsJsonObject().getAsJsonArray("params");
            for (int pi = 0; pi < params.size(); pi++) {
                if (params.get(pi).getAsJsonObject().get("key").getAsString().equals("cver")) {
                    return params.get(pi).getAsJsonObject().get("value").getAsString();
                }
            }
        }
        return DEFAULT_CLIENT_VERSION;
    }

    @Override
    public int extractIntegerFromText(String text) {
        Matcher matcher = TEXT_NUMBER_REGEX.matcher(text);
        if (matcher.find()) return Integer.parseInt(matcher.group(0).replaceAll("[, ']", ""));
        return 0;
    }
}
