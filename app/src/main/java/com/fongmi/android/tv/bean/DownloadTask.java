package com.fongmi.android.tv.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import com.fongmi.android.tv.App;
import com.fongmi.android.tv.db.AppDatabase;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(indices = @Index(value = {"id","url"}, unique = true))
public class DownloadTask {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private Integer id;

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


    @SerializedName("url")
    private String url;

    @SerializedName("hash")
    private String hash;

    @SerializedName("file")
    private Boolean file;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("localPath")
    private String localPath;

    @SerializedName("thumbnailPath")
    private String thumbnailPath;

    @SerializedName("createTime")
    private long createTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public static DownloadTask objectFrom(String str) {
        return App.gson().fromJson(str, DownloadTask.class);
    }

    public static List<DownloadTask> arrayFrom(String str) {
        Type listType = new TypeToken<List<DownloadTask>>() {}.getType();
        List<DownloadTask> items = App.gson().fromJson(str, listType);
        return items == null ? Collections.emptyList() : items;
    }

    public static List<DownloadTask> find(int id) {
        return AppDatabase.get().getDownloadDao().find(id);
    }

    public void save() {
        AppDatabase.get().getDownloadDao().insert(this);
    }

    @NonNull
    @Override
    public String toString() {
        return App.gson().toJson(this);
    }
}