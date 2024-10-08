package com.github.kiulian.downloader;

import com.github.kiulian.downloader.cipher.CachedCipherFactory;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.DownloaderImpl;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.extractor.ExtractorImpl;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.parser.Parser;
import com.github.kiulian.downloader.parser.ParserImpl;

import okhttp3.OkHttpClient;

public class YoutubeDownloader {

    private final Parser parser;

    public YoutubeDownloader(OkHttpClient client) {
        this(Config.buildDefault(), client);
    }

    public YoutubeDownloader(Config config, OkHttpClient client) {
        Downloader downloader = new DownloaderImpl(config, client);
        this.parser = new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader));
    }

    public Response<VideoInfo> getVideoInfo(RequestVideoInfo request) {
        return parser.parseVideo(request);
    }

    public Response<PlaylistInfo> getPlaylistInfo(RequestPlaylistInfo request) {
        return parser.parsePlaylist(request);
    }
}
