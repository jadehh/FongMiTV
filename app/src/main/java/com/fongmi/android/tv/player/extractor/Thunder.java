package com.fongmi.android.tv.player.extractor;

import android.net.Uri;
import android.os.SystemClock;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.Episode;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.exception.ExtractException;
import com.fongmi.android.tv.player.DownloadSource;
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
import java.util.regex.Pattern;

public class Thunder implements Source.Extractor, DownloadSource.Extractor {
    private static final String TAG = Thunder.class.getSimpleName();
    private GetTaskId taskId;

    @Override
    public boolean downloadMatch(String scheme, String host,String url) {
        return match(scheme,host);
//        return "magnet".equals(scheme) || "ed2k".equals(scheme) || "https".equals(scheme) || "http".equals(scheme);
    }

    @Override
    public boolean match(String scheme, String host) {
        return "magnet".equals(scheme) || "ed2k".equals(scheme);
    }

    @Override
    public String fetch(String url) throws Exception {
        return UrlUtil.scheme(url).equals("magnet") ? addTorrentTask(Uri.parse(url)) : addThunderTask(url);
    }

    @Override
    public List<DownloadTask> startDownload(String name, String url,String thumbPath) {
        List<DownloadTask> downloadTasks = new ArrayList<>();
        if (UrlUtil.scheme(url).equals("magnet")) {
            boolean isTorrent = Sniffer.isTorrent(url);
            GetTaskId taskId = XLTaskHelper.get().parse(url, Path.thunder(Util.md5(url)));
            if (isTorrent) {
                Logger.t(TAG).t(String.format("torrent:%s", taskId.getSaveFile()));
                Download.create(url, taskId.getSaveFile()).start();
                return null;
            } else if(taskId.getTaskId() == 0) {
                Uri uri = Uri.parse(url);
                downloadTasks.add(downloadTorrentTask(url,uri,thumbPath));
            }else{
                int time = 0;
                while (XLTaskHelper.get().getTaskInfo(taskId).getTaskStatus() != 2 && time < 5000) {
                    sleep(time);
                    time = time + 1000;
                }
                List<TorrentFileInfo> medias = XLTaskHelper.get().getTorrentInfo(taskId.getSaveFile()).getMedias();
                downloadTasks.add(getDownloadTask(name,taskId,thumbPath));
                for (TorrentFileInfo media : medias) {
                    downloadTasks.add(downloadTorrentTask("",Uri.parse(media.getPlayUrl()),thumbPath));
                }
                XLTaskHelper.get().stopTask(taskId);
            }
        } else {
            downloadTasks.add(downloadThunderTask(name,url,thumbPath));
        }
        return downloadTasks;
    }

    @Override
    public DownloadTask getDownloadingTask(DownloadTask task) {
        XLTaskInfo taskInfo = XLTaskHelper.get().getDwonloadTaskInfo(task.getTaskId());
        task.setTaskId(taskInfo.mTaskId);
        task.setTaskStatus(taskInfo.mTaskStatus);
        task.setDownloadSpeed(taskInfo.mDownloadSpeed);
        if (taskInfo.mTaskId != 0) {
            if (taskInfo.mFileSize == taskInfo.mDownloadSize && taskInfo.mFileSize !=0){
                task.setTaskId(0);
                task.setTaskStatus(Constant.DOWNLOAD_SUCCESS);
            } else if (taskInfo.mFileSize < 0) {
                stopDownload(task);
                task.setTaskId(0);
                task.setTaskStatus(Constant.DOWNLOAD_FAIL);
            }
            task.setFileSize(taskInfo.mFileSize);
            task.setDownloadSize(taskInfo.mDownloadSize);
        } else {
            task.setTaskId(0);
            task.setTaskStatus(Constant.DOWNLOAD_STOP);
        }
        task.update();
        return task;
    }

    @Override
    public List<DownloadTask> resumeDownload(DownloadTask task) {
        return  startDownload(task.getFileName(),task.getUrl(),task.getThumbnailPath());
    }

    @Override
    public void stopDownload(DownloadTask task) {
        if (task.getFile()){
            List<DownloadTask> tasks= AppDatabase.get().getDownloadTaskDao().find(task.getId());
            for (DownloadTask downloadTask:tasks){
                XLTaskHelper.get().stopDownloadTask(downloadTask.getTaskId());
            }
        }else{
            XLTaskHelper.get().stopDownloadTask(task.getTaskId());
        }
    }

    @Override
    public void delete(DownloadTask task) {
        if (task.getFile()){
            List<DownloadTask> tasks= AppDatabase.get().getDownloadTaskDao().find(task.getId());
            for (DownloadTask downloadTask:tasks){
                new File(downloadTask.getLocalPath()).delete();
            }
        }else{
            new File(task.getLocalPath()).delete();
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

    private int sleep(int time) {
        SystemClock.sleep(10);
        time += 10;
        return time;
    }

    private DownloadTask getDownloadTask(String name,GetTaskId taskId,String thumbPath) {
        DownloadTask task = new DownloadTask();
        XLTaskInfo taskInfo = XLTaskHelper.get().getDwonloadTaskInfo(taskId.mTaskId);
        task.setTaskType(Constant.THUNDER_DOWNLOAD_TYPE);
        task.setTaskStatus(taskInfo.mTaskStatus);
        task.setFileSize(taskInfo.mFileSize);
        task.setDownloadSize(taskInfo.mDownloadSize);
        task.setDownloadSpeed(taskInfo.mDownloadSpeed);
        task.setTaskId(taskId.getTaskId());
        task.setUrl(taskId.getRealUrl());
        if (name.length() > 0) task.setFileName(name);
        else  task.setFileName(taskId.getFileName());
        task.setThumbnailPath(thumbPath);
        task.setLocalPath(taskId.getSaveFile().getAbsolutePath());
        task.setTaskStatus(Constant.DOWNLOAD_CONNECTION);
        return task;
    }

    private DownloadTask downloadTorrentTask(String url,Uri uri,String thumbPath) {
        File torrent = new File(uri.getPath());
        String name = uri.getQueryParameter("name");
        int index = Integer.parseInt(uri.getQueryParameter("index"));
        taskId = XLTaskHelper.get().addTorrentTask(torrent, Objects.requireNonNull(torrent.getParentFile()), index);
        if (taskId.mTaskId > 0) {
            while (true) {
                XLTaskInfo taskInfo = XLTaskHelper.get().getBtSubTaskInfo(taskId, index).mTaskInfo;
                if (taskInfo.mTaskStatus == 3) break;
                if (taskInfo.mTaskStatus != 0) break;
                else SystemClock.sleep(300);
            }
            if (url.length() > 0){
                taskId.mRealUrl = url;
            }else{
                taskId.mRealUrl = uri.getPath();
            }
            taskId.mFileName = name;
        }
        return getDownloadTask("",taskId,thumbPath);
    }


    private DownloadTask downloadThunderTask(String name,String url,String thumbPath) {
        File folder = Path.thunder(Util.md5(url));
        taskId = XLTaskHelper.get().addThunderTask(url, folder);
        return getDownloadTask(name,taskId,thumbPath);
    }



    public static class Parser implements Callable<List<Episode>> {

        private static final Pattern THUNDER = Pattern.compile("(magnet|thunder|ed2k):.*");
        private final String url;
        private int time;

        public static boolean match(String url) {
            return THUNDER.matcher(url).find() || isTorrent(url);
        }

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

        private static boolean isTorrent(String url) {
            return !url.startsWith("magnet") && url.split(";")[0].endsWith(".torrent");
        }

        @Override
        public List<Episode> call() {
            boolean torrent = isTorrent(url);
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