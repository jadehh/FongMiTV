package com.fongmi.android.tv.utils;

import android.app.Activity;
import com.fongmi.android.tv.bean.Result;

import java.util.Map;

public class Downloader {

    private Result result;
    private Activity activity;
    private String title;
    private String image;

    private static class Loader {
        static volatile Downloader INSTANCE = new Downloader();
    }

    public static Downloader get() {
        return Loader.INSTANCE;
    }

    public Downloader title(String title) {
        this.title = title;
        return this;
    }
    public Downloader image(String image) {
        this.image = image;
        return this;
    }

    public Downloader result(Result result) {
        this.result = result;
        return this;
    }

    public void start(Activity activity) {
        this.activity = activity;
        if (result.hasMsg()) {
            Notify.show(result.getMsg());
        }  else {
            download();
        }
    }

    private void download() {
        download(result.getHeaders(), result.getRealUrl());
    }

    private void download(Map<String, String> headers, String url) {
        IDMUtil.downloadFile(activity, UrlUtil.fixDownloadUrl(url), title, headers, false, false);
    }

}
