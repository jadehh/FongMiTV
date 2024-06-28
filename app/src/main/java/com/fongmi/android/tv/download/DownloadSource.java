package com.fongmi.android.tv.download;

import android.os.SystemClock;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.Download;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.event.DownloadEvent;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.player.Source;
import com.fongmi.android.tv.player.extractor.JianPian;
import com.fongmi.android.tv.player.extractor.Thunder;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.UrlUtil;
import com.github.catvod.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DownloadSource {

    private final List<Extractor> extractors;

    public DownloadSource() {
        extractors = new ArrayList<>();
        extractors.add(new Thunder());
        extractors.add(new JianPian());
    }


    public void initDownload(Callback callback){
        for (Extractor extractor : extractors){
            extractor.downloadInit();
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

    public void startTask(String name, List<Download> downloads) {
        DownloadTask fileDownloadTask = new DownloadTask();
        fileDownloadTask.setFileName(name);
        String fileUrl = "";
        for (Download download : downloads){
            fileUrl = fileUrl + download.getDownloadUrl() + download.getDownloadName();
        }
        fileDownloadTask.setUrl(Util.md5(fileUrl));
        fileDownloadTask.setFile(true);
        fileDownloadTask.setTaskType(Constant.CUSTOM_DOWNLOAD_TYPE);
        fileDownloadTask.update();
        fileDownloadTask.reload();
        int error_index = 0;
        for (Download download :downloads){
            List<DownloadTask> tasks = download(download.getDownloadName(), download.getDownloadUrl());
            if (tasks != null){
                saveDB(fileDownloadTask.getId(),tasks);
            }else{
                error_index = error_index + 1;
            }
            SystemClock.sleep(5000);
        }
        if (error_index == downloads.size()) fileDownloadTask.delete();
    }

    public void startTask(String name, String url) {
        List<DownloadTask> tasks = download(name,url);
        if (tasks != null){
            saveDB(tasks);
        }
    }

    private List<DownloadTask>  download(String name,String url){
        if (AppDatabase.get().getDownloadTaskDao().find(url).size() > 0){
            DownloadEvent.error(String.format("%s\n%s",name,ResUtil.getString(R.string.download_exists)));
        }else{
            Extractor extractor = getExtractor(url);
            if (extractor != null) {
                List<DownloadTask> tasks = extractor.startDownload(name,url);
                if (tasks != null){
                    DownloadEvent.success(String.format("%s\n%s",name,ResUtil.getString(R.string.download_success_msg)));
                    return tasks;
                }else{
                    DownloadEvent.error(String.format("%s\n%s",name,ResUtil.getString(R.string.download_fail_msg)));
                }
            }else{
                DownloadEvent.error(String.format("%s\n%s",name,ResUtil.getString(R.string.download_not_support_msg)));
            }
        }
        return null;
    }

    private void stop(DownloadTask task,boolean isFinish){
        if (task.getTaskType() != Constant.CUSTOM_DOWNLOAD_TYPE){
            extractors.get(task.getTaskType()).stopDownload(task);
        }
        if (isFinish || task.getTaskStatus() == Constant.DOWNLOAD_SUCCESS){
            task.setTaskId(0);
            task.setDownloadSpeed(0);
            task.setTaskStatus(Constant.DOWNLOAD_SUCCESS);
        }
        else task.setTaskStatus(Constant.DOWNLOAD_STOP);
        task.update();
    }

    public void stopTask(DownloadTask task,boolean isFinish) {
        //如果是文件夹需要全部停止
        if (task.getFile()){
            List <DownloadTask> tasks = AppDatabase.get().getDownloadTaskDao().find(task.getId());
            for (DownloadTask downloadTask:tasks){
                stop(downloadTask,isFinish);
            }
        }
        stop(task,isFinish);
    }


    private void resume(DownloadTask task,int parentId){
        if (task.getTaskType() != Constant.CUSTOM_DOWNLOAD_TYPE){
            List<DownloadTask> tasks = extractors.get(task.getTaskType()).resumeDownload(task);
            if (parentId != -1){
                for (DownloadTask downloadTask:tasks){
                    saveDB(parentId,downloadTask);
                }
            }else{
                saveDB(tasks);
            }
        }
    }

    public void resumeTask(DownloadTask task) {
        if (task.getFile() && task.getTaskType() == Constant.CUSTOM_DOWNLOAD_TYPE){
            List <DownloadTask> tasks = AppDatabase.get().getDownloadTaskDao().find(task.getId());
            for (DownloadTask downloadTask:tasks){
                resume(downloadTask,task.getId());
            }
        }
        resume(task,-1);
    }

    public void getDownloadingTask(DownloadTask task) {
        extractors.get(task.getTaskType()).getDownloadingTask(task);
    }


    private void delete(DownloadTask task){
        if (task.getTaskType() != Constant.CUSTOM_DOWNLOAD_TYPE){
            stopTask(task,false);
            extractors.get(task.getTaskType()).delete(task);
        }
        if (task.getParentId() != 0){
            List<DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().findById(task.getParentId());
            if (downloadTasks.size() > 0){
                DownloadTask parentTask = downloadTasks.get(0);
                long fileSize = parentTask.getFileSize() - task.getFileSize();
                if (fileSize == 0) parentTask.delete();
                else{
                    parentTask.setFileSize(fileSize);
                    parentTask.update();
                }
            }
        }
        task.delete();
    }

    public void deleteTask(DownloadTask task){
        if (task.getFile()){
            List <DownloadTask> tasks = AppDatabase.get().getDownloadTaskDao().find(task.getId());
            for (DownloadTask downloadTask:tasks){
                delete(downloadTask);
            }
        }
        delete(task);
    }

    private void saveDB(List<DownloadTask> downloadTasks) {
        if (downloadTasks != null) {
            if (downloadTasks.size() > 1) {
                saveMultipleDB(downloadTasks);
            } else if (downloadTasks.size() == 1) {
                DownloadTask task = downloadTasks.get(0);
                task.save();
            }
        }
    }

    private void setParentId(int parentId,DownloadTask downloadTasks){
        downloadTasks.setParentId(parentId);
        downloadTasks.save();
    }

    private void saveDB(int parentId,List<DownloadTask> downloadTasks) {
        DownloadTask task = downloadTasks.get(0);
        setParentId(parentId,task);
    }

    private void saveDB(int parentId,DownloadTask downloadTask) {
       setParentId(parentId,downloadTask);
    }

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

        void downloadInit();

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
