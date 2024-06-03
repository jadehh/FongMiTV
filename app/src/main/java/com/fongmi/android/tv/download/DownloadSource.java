package com.fongmi.android.tv.download;

import com.fongmi.android.tv.player.extractor.JianPian;
import com.fongmi.android.tv.player.extractor.Thunder;
import com.fongmi.android.tv.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadSource {

    private final List<Extractor> extractors;

    private static class Loader {
        static volatile DownloadSource INSTANCE = new DownloadSource();
    }

    public static DownloadSource get() {
        return Loader.INSTANCE;
    }

    public DownloadSource() {
        extractors = new ArrayList<>();
        extractors.add(new JianPian());
        extractors.add(new Thunder());

    }

    private Extractor getExtractor(String url) {
        String host = UrlUtil.host(url);
        String scheme = UrlUtil.scheme(url);
        for (Extractor extractor : extractors) if (extractor.match(scheme, host)) return extractor;
        return null;
    }

    public void download(String url) throws Exception {
        Extractor extractor = getExtractor(url);
        if (extractor != null) extractor.download(url);
    }

    public void stop() {
        if (extractors == null) return;
        for (Extractor extractor : extractors) extractor.stop();
    }

    public void exit() {
        if (extractors == null) return;
        for (Extractor extractor : extractors) extractor.exit();
    }

    public interface Extractor {

        boolean match(String scheme, String host);


        public void download(String url) throws Exception;

        void stop();

        void exit();
    }
}
