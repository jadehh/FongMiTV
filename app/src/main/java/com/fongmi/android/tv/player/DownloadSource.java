package com.fongmi.android.tv.player;

import android.content.Context;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.Download;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.player.extractor.JianPian;
import com.fongmi.android.tv.player.extractor.M3U8;
import com.fongmi.android.tv.player.extractor.Thunder;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadSource {
    private final List<Extractor> extractors;

    private  JianPian jianPian;

    private M3U8 m3U8;
    public DownloadSource() {
        extractors = new ArrayList<>();
        extractors.add(new Thunder());
        jianPian = new JianPian();
        extractors.add(jianPian);
        m3U8 = new M3U8();
        extractors.add(m3U8);
    }
    public void initDownload(Context context){
        m3U8.init(context);
        jianPian.setP2PClass(Source.get().getP2PClass());
        jianPian.setPathPaused(Source.get().getPathPaused());
    }
    public void initDownload(Context context,Callback callback){
        m3U8.init(context);
        jianPian.setP2PClass(Source.get().getP2PClass());
        jianPian.setPathPaused(Source.get().getPathPaused());
        callback.success();
    }

    public static DownloadSource get() {
        return Loader.INSTANCE;
    }
    private Extractor getExtractor(String url) {
        String host = UrlUtil.host(url);
        String scheme = UrlUtil.scheme(url);
        for (Extractor extractor : extractors)
            if (extractor.downloadMatch(scheme, host,url)) return extractor;
        return extractors.get(0);
    }
//    public void startTask(String name, List<Download> downloads,String thumbPath) {
//        DownloadTask fileDownloadTask = getFileDownloadTask(name,thumbPath);
//        int error_index = 0;
//        String url = "";
//        for (Download download : downloads){
//            List<DownloadTask> tasks = download(download);
//            url = url + download.getUrl();
//            if (tasks != null){
//                saveDB(fileDownloadTask.getId(),tasks);
//            }else{
//                error_index = error_index + 1;
//            }
//            SystemClock.sleep(5000);
//        }
//        fileDownloadTask.setUrl(Util.md5(url));
//        fileDownloadTask.update();
//        if (error_index == downloads.size()) fileDownloadTask.delete();
//    }

    private DownloadTask getFileDownloadTask(String name,String thumbPath){
        DownloadTask fileDownloadTask = new DownloadTask();
        fileDownloadTask.setFileName(name);
        fileDownloadTask.setThumbnailPath(thumbPath);
        fileDownloadTask.setUrl(name);
        fileDownloadTask.setFile(true);
        fileDownloadTask.setTaskType(Constant.CUSTOM_DOWNLOAD_TYPE);
        fileDownloadTask.update();
        fileDownloadTask.reload();
        return fileDownloadTask;
    }

//    public void startTask(Download download) {
//        List<DownloadTask> tasks = download(download);
//        if (tasks != null){
//            saveDB(tasks);
//        }
//    }
//

    public String startTask(Download download){
        return download(download);
    }

    private String download(Download download){
        if (AppDatabase.get().getDownloadTaskDao().find(download.getUrl()).size() > 0){
            return ResUtil.getString(R.string.download_exists);
        }else{
            Extractor extractor = getExtractor(download.getUrl());
            if (extractor != null) {
                List<DownloadTask> tasks = extractor.startDownload(download.getVodName(),download.getUrl(),download.getVodPic());
                if (tasks != null){
                    saveDB(tasks);
                    return ResUtil.getString(R.string.download_success_msg);
                }else  return ResUtil.getString(R.string.download_fail_msg);
            } return ResUtil.getString(R.string.download_not_support_msg);
        }

    }


//    private List<DownloadTask>  download(Download download){
//        if (AppDatabase.get().getDownloadTaskDao().find(download.getUrl()).size() > 0){
//            DownloadEvent.error(String.format("%s\n%s",download.getVodName(),ResUtil.getString(R.string.download_exists)));
//        }else{
//            Extractor extractor = getExtractor(download.getUrl());
//            if (extractor != null) {
//                List<DownloadTask> tasks = extractor.startDownload(download.getVodName(),download.getUrl(),download.getVodPic());
//                if (tasks != null){
//                    DownloadEvent.success(String.format("%s\n%s",download.getVodName(),ResUtil.getString(R.string.download_success_msg)));
//                    return tasks;
//                }else{
//                    DownloadEvent.error(String.format("%s\n%s",download.getVodName(),ResUtil.getString(R.string.download_fail_msg)));
//                }
//            }else{
//                DownloadEvent.error(String.format("%s\n%s",download.getVodName(),ResUtil.getString(R.string.download_not_support_msg)));
//            }
//        }
//        return null;
//    }

    private void stop(DownloadTask task,boolean isFinish){
        if (task.getTaskType() != Constant.CUSTOM_DOWNLOAD_TYPE){
            extractors.get(task.getTaskType()).stopDownload(task);
        }
        if (isFinish || task.getTaskStatus() == Constant.DOWNLOAD_SUCCESS){
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

    public DownloadTask getDownloadingTask(DownloadTask task) {
        return extractors.get(task.getTaskType()).getDownloadingTask(task);
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

        boolean downloadMatch(String scheme, String host,String url);

        boolean match(String scheme, String host);

        String fetch(String url) throws Exception;

        List<DownloadTask> startDownload(String name, String url,String thumbPath);

        DownloadTask getDownloadingTask(DownloadTask task);

        List<DownloadTask> resumeDownload(DownloadTask task);

        void stopDownload(DownloadTask task);

        void delete(DownloadTask task);

        void stop();

        void exit();

    }

    private static class Loader {
        static volatile DownloadSource INSTANCE = new DownloadSource();
    }
}