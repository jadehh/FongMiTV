package com.fongmi.android.tv.download;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.Msg;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.event.MessageEvent;
import com.fongmi.android.tv.impl.TaskModelImp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DownloadUpdateUI {
    private static final String TAG = DownloadUpdateUI.class.getSimpleName();
    private static DownloadUpdateUI downloadUpdateUI;

    private List<DownloadTask> downloadingTasks;

    private List<DownloadTask> downloadSuccessTasks;

    private TaskModelImp taskModelImp;


    public DownloadUpdateUI() {
        downloadingTasks = new ArrayList<>();
        downloadSuccessTasks = new ArrayList<>();
        taskModelImp = new TaskModelImp();
    }

    public static synchronized DownloadUpdateUI getInstance() {
        if (downloadUpdateUI == null) {
            downloadUpdateUI = new DownloadUpdateUI();
        }
        return downloadUpdateUI;
    }


    private void downloading(){
        downloadingTasks.clear();
        List<DownloadTask> downloadTasks = taskModelImp.findLoadingTask();
        for (DownloadTask task : downloadTasks) {
            int taskStatus = task.getTaskStatus();
            long taskId = task.getTaskId();
            if (taskStatus != Constant.DOWNLOAD_STOP && taskStatus != Constant.DOWNLOAD_WAIT && taskId != 0 && !task.getFile()) {
                DownloadSource.get().getDownloadingTask(task);
            }
            if (task.getFile()){
                setFileDownloadTask(task);
            }
            if (task.getParentId() == 0){
                downloadingTasks.add(task);
            }
        }
        EventBus.getDefault().postSticky(new MessageEvent(new Msg(Constant.DOWNLOAD_UPDATE_MESSAGE_TYPE, downloadingTasks)));
    }


    private void downloadSuccess(){
        downloadSuccessTasks.clear();
        List<DownloadTask> downloadTasks = taskModelImp.findSuccessTask();
        for (DownloadTask task : downloadTasks) {
            if (task.getParentId() == 0){
                downloadSuccessTasks.add(task);
            }
            if (task.getTaskId() != 0){
                finishTask(task);
            }
        }
        EventBus.getDefault().postSticky(new MessageEvent(new Msg(Constant.DOWNLOAD_Success_MESSAGE_TYPE, downloadSuccessTasks)));
    }

    public void downUpdate() {
       downloading();
       downloadSuccess();
    }

    public void setFileDownloadTask(DownloadTask fileDownloadTask) {
        List<DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().find(fileDownloadTask.getId());
        long downloadSpeed = 0;
        long downloadSize = 0;
        int downloadStatus = -1;
        for (DownloadTask downloadTask:downloadTasks){
            downloadSize = downloadSize + downloadTask.getDownloadSize();
            downloadSpeed = downloadSpeed + downloadTask.getDownloadSpeed();
            downloadStatus = downloadTask.getTaskStatus();
        }
        fileDownloadTask.setDownloadSize(downloadSize);
        fileDownloadTask.setDownloadSpeed(downloadSpeed/downloadTasks.size());
        fileDownloadTask.setTaskStatus(downloadStatus);
        fileDownloadTask.update();
    }


    private void finishTask(DownloadTask task){
        DownloadSource.get().stopDownload(task,true);
    }
}


