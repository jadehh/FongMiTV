package com.fongmi.android.tv.service;

import android.os.SystemClock;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.DownloadMsg;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.event.DownloadEvent;
import com.fongmi.android.tv.player.DownloadSource;

import java.util.ArrayList;
import java.util.List;

public class DownloadService {
    private static final String TAG = DownloadService.class.getSimpleName();
    private static DownloadService downloadService;

    private List<DownloadTask> downloadingTasks;

    private List<DownloadTask> downloadSuccessTasks;

    private static boolean isLoopDown = true;

    public DownloadService() {
        downloadingTasks = new ArrayList<>();
        downloadSuccessTasks = new ArrayList<>();
    }

    public static synchronized DownloadService getInstance() {
        if (downloadService == null) {
            downloadService = new DownloadService();
        }
        return downloadService;
    }


    private void downloading(){
        downloadingTasks.clear();
        List<DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().findLoadingTask(Constant.DOWNLOAD_SUCCESS);
        for (DownloadTask task : downloadTasks) {
            int taskStatus = task.getTaskStatus();
            long taskId = task.getTaskId();
            if (taskStatus != Constant.DOWNLOAD_STOP  && taskId != 0 && !task.getFile()) {
                task = DownloadSource.get().getDownloadingTask(task);
            }
            if (task.getFile()){
                setFileDownloadTask(task);
            }
            if (task.getParentId() == 0){
                downloadingTasks.add(task);
            }
        }
        DownloadEvent.postDownloadMsg(new DownloadMsg(Constant.DOWNLOAD_UPDATE_MESSAGE_TYPE, downloadingTasks));
    }


    private void downloadSuccess(){
        downloadSuccessTasks.clear();
        List<DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().findSuccessTask(Constant.DOWNLOAD_SUCCESS);
        for (DownloadTask task : downloadTasks) {
            if (task.getParentId() == 0){
                downloadSuccessTasks.add(task);
            }
            if (task.getTaskId() != 0){
                finishTask(task);
            }
        }
        DownloadEvent.postDownloadMsg(new DownloadMsg(Constant.DOWNLOAD_Success_MESSAGE_TYPE, downloadSuccessTasks));
    }

    public Runnable updateDownloadService() {
        while (isIsLoopDown()){
            downloading();
            downloadSuccess();
            SystemClock.sleep(500);
        }
        return null;
    }

    public void refreshDownloading(){
        downloading();
    }

    public void refreshDownloadFinish(){
        downloadSuccess();
    }

    private void setFileDownloadTask(DownloadTask fileDownloadTask) {
        List<DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().find(fileDownloadTask.getId());
        if (downloadTasks.size() != 0) {
            long downloadSpeed = 0;
            long downloadSize = 0;
            long fileSize = 0;
            int downloadStatus = Constant.DOWNLOAD_SUCCESS;
            for (DownloadTask downloadTask : downloadTasks) {
                fileSize = fileSize + downloadTask.getFileSize();
                downloadSize = downloadSize + downloadTask.getDownloadSize();
                downloadSpeed = downloadSpeed + downloadTask.getDownloadSpeed();
                if (downloadTask.getTaskStatus() != Constant.DOWNLOAD_SUCCESS) {
                    downloadStatus = downloadTask.getTaskStatus();
                }
            }
            fileDownloadTask.setFileSize(fileSize);
            fileDownloadTask.setDownloadSize(downloadSize);
            fileDownloadTask.setDownloadSpeed(downloadSpeed / downloadTasks.size());
            fileDownloadTask.setTaskStatus(downloadStatus);
            fileDownloadTask.update();
        }
    }
    private void finishTask(DownloadTask task){
        DownloadSource.get().stopTask(task,true);
    }

    private boolean isIsLoopDown() {
        return isLoopDown;
    }

}