package com.fongmi.android.tv.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import com.fongmi.android.tv.App;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.utils.Download;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity(indices = @Index(value = {"id", "url"}, unique = true))
public class DownloadTask {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private Integer id;

    @SerializedName("parentId")
    private int parentId;

    @SerializedName("taskId")
    private long taskId;
    @SerializedName("taskStatus")
    private int taskStatus;
    @SerializedName("fileSize")
    private long fileSize;

    @SerializedName("taskType")
    private int taskType;

    @SerializedName("downloadSize")
    private long downloadSize;

    @SerializedName("downloadSpeed")
    private long downloadSpeed;

    @SerializedName("percent")
    private  int percent;


    @SerializedName("url")
    private String url;

    @SerializedName("hash")
    private String hash;

    @SerializedName("file")
    private Boolean file = false;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("localPath")
    private String localPath;

    @SerializedName("thumbnailPath")
    private String thumbnailPath;

    @SerializedName("createTime")
    private long createTime;

    @SerializedName("updateTime")
    private long updateTime;

    public static DownloadTask objectFrom(String str) {
        return App.gson().fromJson(str, DownloadTask.class);
    }

    public static List<DownloadTask> arrayFrom(String str) {
        Type listType = new TypeToken<List<DownloadTask>>() {
        }.getType();
        List<DownloadTask> items = App.gson().fromJson(str, listType);
        return items == null ? Collections.emptyList() : items;
    }

    public static List<DownloadTask> find(String url) {
        return AppDatabase.get().getDownloadTaskDao().find(url);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getFile() {
        return file;
    }

    public void setFile(Boolean file) {
        this.file = file;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void save() {
        if (AppDatabase.get().getDownloadTaskDao().find(this.url).size() > 0) update();
        else insert();
    }

    public void update() {
        if (this.id == null) {
            List <DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().find(this.url);
            if (downloadTasks.size() > 0)   {
                this.id =downloadTasks.get(0).getId();
            }
            else {
                this.insert();
                return;
            }
        }
        this.updateTime = new Date().getTime();
        AppDatabase.get().getDownloadTaskDao().update(this);
    }

    public void  reload(){
        List <DownloadTask> downloadTasks = AppDatabase.get().getDownloadTaskDao().find(this.url);
        this.id = downloadTasks.get(0).getId();
    }



    public void delete(){
        if (this.getFile()){
            List<DownloadTask> tasks= AppDatabase.get().getDownloadTaskDao().find(this.getId());
            for (DownloadTask task:tasks){
                AppDatabase.get().getDownloadTaskDao().delete(task.getId());
            }
        }
        AppDatabase.get().getDownloadTaskDao().delete(this.getId());
    }

    public void insert() {
        this.createTime = new Date().getTime();
        AppDatabase.get().getDownloadTaskDao().insert(this);
    }

    public List<DownloadTask> getSubDownloadTasks() {
        if (this.getFile()){
            return AppDatabase.get().getDownloadTaskDao().find(this.getId());
        }else{
            return AppDatabase.get().getDownloadTaskDao().find(this.getUrl());
        }
    }

    @NonNull
    @Override
    public String toString() {
        return App.gson().toJson(this);
    }
}