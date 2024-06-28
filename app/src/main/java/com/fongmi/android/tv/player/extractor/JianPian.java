package com.fongmi.android.tv.player.extractor;

import android.net.Uri;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.player.Source;
import com.github.catvod.utils.Path;
import com.orhanobut.logger.Logger;
import com.p2p.P2PClass;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JianPian implements Source.Extractor, DownloadSource.Extractor {

    private P2PClass p2p;
    private String path;
    private Map<String, Boolean> pathPaused;

    private static final String TAG = JianPian.class.getSimpleName();

    @Override
    public boolean match(String scheme, String host) {
        return "tvbox-xg".equals(scheme) || "jianpian".equals(scheme) || "ftp".equals(scheme);
    }

    @Override
    public boolean downloadMatch(String scheme, String host) {
        return this.match(scheme,host);
    }

    @Override
    public List<DownloadTask> startDownload(String name,String url) {try {
            long taskId = p2p.P2Pdownload(getPathBytes(url));
            List <DownloadTask> taskList = new ArrayList<>();
            DownloadTask downloadTask = new DownloadTask();
            List <DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().find(url);
            if (downloadTasks.size() > 0 ) downloadTask = downloadTasks.get(0);
            if (name.length() > 0) downloadTask.setFileName(name);
            else downloadTask.setFileName(getFileName(url));
            downloadTask.setTaskType(Constant.JIANPIAN_DOWNLOAD_TYPE);
            downloadTask.setTaskStatus(Constant.DOWNLOAD_LOADING);
            downloadTask.setLocalPath(this.getLocalPath(url));
            downloadTask.setUrl(url);
            downloadTask.setTaskId(taskId);
            taskList.add(downloadTask);
            return taskList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DownloadTask> resumeDownload(DownloadTask task) {
        return  startDownload(task.getFileName(),task.getUrl());
    }

    @Override
    public void stopDownload(DownloadTask task) {
        p2p.P2Pdoxpause(getPathBytes(task.getUrl()));
    }

    private void init() {
        if (p2p == null) p2p = new P2PClass();
        if (pathPaused == null) pathPaused = new HashMap<>();
    }

    @Override
    public void downloadInit(){
        Logger.t(TAG).d("初始化荐片下载器");
        if (p2p == null) p2p = new P2PClass();
        Logger.t(TAG).d("荐片下载器初始化完成");
    }

    @Override
    public String fetch(String url) throws Exception {
        init();
        stop();
        start(url);
        return "http://127.0.0.1:" + p2p.port + "/" + URLEncoder.encode(Uri.parse(path).getLastPathSegment(), "GBK");
    }

    public String getPath(String url){
        String downloadPath = URLDecoder.decode(url).split("\\|")[0];
        downloadPath = downloadPath.replace("jianpian://pathtype=url&path=", "");
        downloadPath = downloadPath.replace("tvbox-xg://", "").replace("tvbox-xg:", "");
        downloadPath = downloadPath.replace("xg://", "ftp://").replace("xgplay://", "ftp://");
        return downloadPath;
    }

    public String getFileName(String url){
        String[] nameList = URLDecoder.decode(url).split("/");
        return nameList[nameList.length-1];
    }

    public byte[] getPathBytes(String url){
        try {
            return getPath(url).getBytes("GBK") ;
        }catch (Exception e){
            return null;
        }
    }

    public String getLocalPath(String url){
        try {
            return "http://127.0.0.1:" + p2p.port + "/" + URLEncoder.encode(Uri.parse(getPath(url)).getLastPathSegment(), "GBK");
        }catch (Exception e){
            return null;
        }
    }
    private void start(String url) {
        try {
            String lastPath = path;
            path = URLDecoder.decode(url).split("\\|")[0];
            path = path.replace("jianpian://pathtype=url&path=", "");
            path = path.replace("tvbox-xg://", "").replace("tvbox-xg:", "");
            path = path.replace("xg://", "ftp://").replace("xgplay://", "ftp://");
            boolean isDiff = lastPath != null && !lastPath.equals(path);
            if (isDiff) p2p.P2Pdoxdel(lastPath.getBytes("GBK"));
            p2p.P2Pdoxstart(path.getBytes("GBK"));
            if (lastPath == null || isDiff) p2p.P2Pdoxadd(path.getBytes("GBK"));
            if (isDiff && pathPaused.containsKey(lastPath)) pathPaused.remove(lastPath);
            pathPaused.put(path, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (p2p == null || path == null) return;
            if (pathPaused.containsKey(path) && pathPaused.get(path)) return;
            p2p.P2Pdoxpause(path.getBytes("GBK"));
            pathPaused.put(path, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(DownloadTask task) {
        p2p.P2Pdoxdel(getPathBytes(task.getUrl()));
    }

    @Override
    public void getDownloadingTask(DownloadTask task) {
        long downloadSpeed = p2p.P2Pgetspeed((int) task.getTaskId());
        long downloadSize = p2p.P2Pgetdownsize((int)task.getTaskId());
        long fileSize = p2p.P2Pgetfilesize((int)task.getTaskId());
        if (fileSize == 0 && downloadSize == 0 && downloadSpeed == 0){
            task.setDownloadSpeed(0);
            if (task.getFileSize() != 0){
                task.setTaskStatus(Constant.DOWNLOAD_STOP);
            }else{
                task.setTaskStatus(Constant.DOWNLOAD_WAIT);
            }
        }else{
            task.setDownloadSpeed(downloadSpeed);
            task.setDownloadSize(downloadSize);
            task.setFileSize(fileSize);
            if (downloadSize == fileSize ){
                task.setTaskStatus(Constant.DOWNLOAD_SUCCESS);
            }
        }
        task.update();

    }

    @Override
    public void exit() {
        Path.clear(Path.jpa());
    }
}
