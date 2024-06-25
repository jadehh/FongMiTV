package com.fongmi.android.tv.download;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.player.extractor.JianPian;
import com.fongmi.android.tv.player.extractor.Thunder;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadSource {

    private final List<Extractor> extractors;

    public DownloadSource() {
        extractors = new ArrayList<>();
        extractors.add(new Thunder());
    }


    public void addJianpianExtractor(Callback callback){
        if (extractors.size() != 2){
            JianPian jianPian = new JianPian();
            jianPian.downloadInit();
            extractors.add(jianPian);
        }
        callback.success();
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

    public void download(String name,String url,Callback callback){
        Extractor extractor = getExtractor(url);
        if (extractor != null) {
            List<DownloadTask> tasks = extractor.startDownload(name,url);
            if (tasks != null){
                saveDB(tasks);
                App.post(() -> callback.success(ResUtil.getString(R.string.download_success_msg)));
            }else{
                App.post(() -> callback.error(Notify.getError(R.string.error_download, new Exception(ResUtil.getString(R.string.download_fail_msg)))));
            }
        }else{
            App.post(() -> callback.error(Notify.getError(R.string.error_download, new Exception(ResUtil.getString(R.string.download_not_support_msg)))));
        }
    }

    public void startDownload(String name, String url, Callback callback) {
        if (AppDatabase.get().getDownloadTaskDao().find(url).size() > 0){
            App.post(() -> callback.error(Notify.getError(R.string.error_download, new Exception(ResUtil.getString(R.string.download_exists)))));
        }else{
            App.execute(() -> download(name,url,callback));
        }
    }

    public void stopDownload(DownloadTask task,boolean isFinish) {
        extractors.get(task.getTaskType()).stopDownload(task);
        task.setDownloadSpeed(0);
        if (isFinish){
            task.setTaskId(0);
            task.setTaskStatus(Constant.DOWNLOAD_SUCCESS);
        }
        else task.setTaskStatus(Constant.DOWNLOAD_STOP);
        task.update();
    }

    public void getDownloadingTask(DownloadTask task) {
        extractors.get(task.getTaskType()).getDownloadingTask(task);
    }


    public void delete(DownloadTask task){
        extractors.get(task.getTaskType()).delete(task);
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

        List<DownloadTask> startDownload(String name,String url);

        List<DownloadTask> resumeDownload(DownloadTask task);

        void stop();

        void delete(DownloadTask task);

        void getDownloadingTask(DownloadTask task);

        void stopDownload(DownloadTask task);


        void exit();
    }

    private static class Loader {
        static volatile DownloadSource INSTANCE = new DownloadSource();
    }
}
