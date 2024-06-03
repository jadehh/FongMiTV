package com.fongmi.android.tv.player.extractor;

import android.net.Uri;
import android.os.SystemClock;


import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.Episode;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.exception.ExtractException;
import com.fongmi.android.tv.player.Source;
import com.fongmi.android.tv.utils.Download;
import com.fongmi.android.tv.utils.Sniffer;
import com.fongmi.android.tv.utils.UrlUtil;
import com.github.catvod.utils.Path;
import com.github.catvod.utils.Util;
import com.orhanobut.logger.Logger;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class Thunder implements Source.Extractor, DownloadSource.Extractor {

    private GetTaskId taskId;

    private static final String TAG = Thunder.class.getSimpleName();

    private int time = 0;

    @Override
    public boolean match(String scheme, String host) {
        return "magnet".equals(scheme) || "ed2k".equals(scheme);
    }

    private void sleep() {
        SystemClock.sleep(10);
        time += 10;
    }

    @Override
    public String fetch(String url) throws Exception {
        return UrlUtil.scheme(url).equals("magnet") ? addTorrentTask(Uri.parse(url)) : addThunderTask(url);
    }

    @Override
    public void download(String url) throws Exception {
        if (UrlUtil.scheme(url).equals("magnet")) {
            boolean torrent = Sniffer.isTorrent(url);
            GetTaskId taskId = XLTaskHelper.get().parse(url, Path.thunder(Util.md5(url)));
            if (!torrent && !taskId.getRealUrl().startsWith("magnet")){
                Logger.t(TAG).t(String.format("FileName:%s,RealUrl",taskId.getFileName(),taskId.getRealUrl()));
            }
            if (torrent) {
                Logger.t(TAG).t(String.format("torrent:%s",taskId.getSaveFile()));
                Download.create(url, taskId.getSaveFile()).start();
            }
            else while (XLTaskHelper.get().getTaskInfo(taskId).getTaskStatus() != 2 && time < 5000) {
                sleep();
            }
            List<TorrentFileInfo> medias = XLTaskHelper.get().getTorrentInfo(taskId.getSaveFile()).getMedias();
            for (TorrentFileInfo media : medias) {
                downloadTorrentTask(Uri.parse(media.getPlayUrl()));
            }
            XLTaskHelper.get().stopTask(taskId);
        } else{
            downloadThunderTask(url);
        }
    }

    private String addTorrentTask(Uri uri) throws Exception {
        File torrent = new File(uri.getPath());
        String name = uri.getQueryParameter("name");
        int index = Integer.parseInt(uri.getQueryParameter("index"));
        taskId = XLTaskHelper.get().addTorrentTask(torrent, Objects.requireNonNull(torrent.getParentFile()), index);
        while (true) {
            XLTaskInfo taskInfo = XLTaskHelper.get().getBtSubTaskInfo(taskId, index).mTaskInfo;
            if (taskInfo.mTaskStatus == 3) throw new ExtractException(taskInfo.getErrorMsg());
            if (taskInfo.mTaskStatus != 0) return XLTaskHelper.get().getLocalUrl(new File(torrent.getParent(), name));
            else SystemClock.sleep(300);
        }
    }

    private String addThunderTask(String url) {
        File folder = Path.thunder(Util.md5(url));
        taskId = XLTaskHelper.get().addThunderTask(url, folder);
        return XLTaskHelper.get().getLocalUrl(taskId.getSaveFile());
    }


    private void downloadTorrentTask(Uri uri) throws Exception {
        File torrent = new File(uri.getPath());
        String name = uri.getQueryParameter("name");
        int index = Integer.parseInt(uri.getQueryParameter("index"));
        taskId = XLTaskHelper.get().addTorrentTask(torrent, Objects.requireNonNull(torrent.getParentFile()), index);
        if (taskId.mTaskId > 0){
            while (true) {
                XLTaskInfo taskInfo = XLTaskHelper.get().getBtSubTaskInfo(taskId, index).mTaskInfo;
                if (taskInfo.mTaskStatus == 3) throw new ExtractException(taskInfo.getErrorMsg());
                if (taskInfo.mTaskStatus != 0) break;
                else SystemClock.sleep(300);
            }
            taskId.mRealUrl = uri.getPath();
            taskId.mFileName = name;
            updateDownload(taskId);
        }

    }

    private void downloadThunderTask(String url) {
        File folder = Path.thunder(Util.md5(url));
        taskId = XLTaskHelper.get().addThunderTask(url, folder);
        updateDownload(taskId);
    }

    private void updateDownload(GetTaskId taskId){
        DownloadTask down = new DownloadTask();
        down.setTaskStatus(Constant.THUNDER_TYPE);
        down.setTaskId(taskId.getTaskId());
        down.setUrl(taskId.getRealUrl());
        down.setCreateTime(System.currentTimeMillis());
        down.setFileName(taskId.getFileName());
        down.setLocalPath(taskId.getSaveFile().getAbsolutePath());
        down.save();
    }

    @Override
    public void stop() {
        if (taskId == null) return;
        XLTaskHelper.get().deleteTask(taskId);
        taskId = null;
    }

    @Override
    public void exit() {
        XLTaskHelper.get().release();
    }

    public static class Parser implements Callable<List<Episode>> {

        private final String url;
        private int time;

        public static Parser get(String url) {
            return new Parser(url);
        }

        public Parser(String url) {
            this.url = url;
        }

        private void sleep() {
            SystemClock.sleep(10);
            time += 10;
        }

        @Override
        public List<Episode> call() {
            boolean torrent = Sniffer.isTorrent(url);
            List<Episode> episodes = new ArrayList<>();
            GetTaskId taskId = XLTaskHelper.get().parse(url, Path.thunder(Util.md5(url)));
            if (!torrent && !taskId.getRealUrl().startsWith("magnet")) return Arrays.asList(Episode.create(taskId.getFileName(), taskId.getRealUrl()));
            if (torrent) Download.create(url, taskId.getSaveFile()).start();
            else while (XLTaskHelper.get().getTaskInfo(taskId).getTaskStatus() != 2 && time < 5000) sleep();
            List<TorrentFileInfo> medias = XLTaskHelper.get().getTorrentInfo(taskId.getSaveFile()).getMedias();
            for (TorrentFileInfo media : medias) episodes.add(Episode.create(media.getFileName(), media.getSize(), media.getPlayUrl()));
            XLTaskHelper.get().stopTask(taskId);
            return episodes;
        }
    }
}
