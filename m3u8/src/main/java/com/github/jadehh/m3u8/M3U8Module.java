package com.github.jadehh.m3u8;

public class M3U8Module {
    private long speed;
    private long downloadSize;
    private int percent;
    private long fileSize;
    private String fileName;
    private String filePath;
    private int taskType;

    public M3U8Module() {
    }

    public M3U8Module(int taskType, long speed, long downloadSize, long fileSize, int percent, String fileName, String filePath) {
        this.taskType = taskType;
        this.speed = speed;
        this.downloadSize = downloadSize;
        this.fileSize = fileSize;
        this.percent = percent;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
