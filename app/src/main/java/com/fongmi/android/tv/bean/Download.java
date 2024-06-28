package com.fongmi.android.tv.bean;

public class Download {
    private String downloadUrl;

    private String downloadName;

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
