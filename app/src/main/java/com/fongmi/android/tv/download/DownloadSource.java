package com.fongmi.android.tv.download;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.player.extractor.JianPian;
import com.fongmi.android.tv.player.extractor.Thunder;
import com.fongmi.android.tv.utils.Download;
import com.fongmi.android.tv.utils.UrlUtil;
import com.xunlei.downloadlib.parameter.GetTaskId;

import java.util.ArrayList;
import java.util.List;

public class DownloadSource {

    private final List<Extractor> extractors;

    public DownloadSource() {
        extractors = new ArrayList<>();
        extractors.add(new Thunder());
        extractors.add(new JianPian());
    }

    public static DownloadSource get() {
        return Loader.INSTANCE;
    }

    private Extractor getExtractor(String url) {
        String host = UrlUtil.host(url);
        String scheme = UrlUtil.scheme(url);
        for (Extractor extractor : extractors)
            if (extractor.downloadMatch(scheme, host)) return extractor;
        return null;
    }

    public void resumeDownload(DownloadTask task) {
        List<DownloadTask> tasks = extractors.get(task.getTaskType()).resumeDownload(task);
        saveDB(tasks);
    }

    public void startDownload(String url) {
        //支持单文件下载,支持多文件下载,多文件下载,是一个下载文件夹对应多个文件
        Extractor extractor = getExtractor(url);
        if (extractor != null) {
            List<DownloadTask> tasks = extractor.startDownload(url);
            saveDB(tasks);
        }
    }

    public void stopDownload(DownloadTask task,boolean isFinish) {
        List<DownloadTask> downloadTasks = task.getSubDownloadTasks();
        for (DownloadTask downloadTask : downloadTasks) {
            extractors.get(task.getTaskType()).stopDownload(downloadTask);
            downloadTask.setDownloadSpeed(0);
            if (isFinish){
                downloadTask.setTaskId(0);
                downloadTask.setTaskStatus(Constant.DOWNLOAD_SUCCESS);
            }
            else downloadTask.setTaskStatus(Constant.DOWNLOAD_STOP);
            downloadTask.update();
        }
    }
    public void delete(DownloadTask task){
        stopDownload(task,false);
        task.delete();
    }

    private int saveDB(List<DownloadTask> downloadTasks) {
        //多文件下载
        if (downloadTasks != null) {
            if (downloadTasks.size() > 1) {
                saveMultipleDB(downloadTasks);
            } else if (downloadTasks.size() == 1) {
                DownloadTask task = downloadTasks.get(0);
                task.save();
            }
        }
        return 0;
    }

    //多文件存储
    private void saveMultipleDB(List<DownloadTask> downloadTasks) {
        DownloadTask taskFile = downloadTasks.get(0);
        taskFile.setFile(true);
        taskFile.save();
        taskFile = AppDatabase.get().getDownloadTaskDao().find(taskFile.getUrl()).get(0);
        long fileSize = 0;
        for (int i = 1; i < downloadTasks.size(); i++) {
            DownloadTask task = downloadTasks.get(i);
            task.setFile(false);
            taskFile.setTaskStatus(task.getTaskStatus());
            fileSize = fileSize + task.getFileSize();
            task.setParentId(taskFile.getId());
            task.save();
        }
        taskFile.setFileSize(fileSize);
        taskFile.update();
    }

    public void exit() {
        if (extractors == null) return;
        for (Extractor extractor : extractors) extractor.exit();
    }

    public interface Extractor {

        boolean match(String scheme, String host);

        boolean downloadMatch(String scheme, String host);

        List<DownloadTask> startDownload(String url);

        List<DownloadTask> resumeDownload(DownloadTask task);

        void stop();

        void stopDownload(DownloadTask task);

        void exit();
    }

    private static class Loader {
        static volatile DownloadSource INSTANCE = new DownloadSource();
    }
}
