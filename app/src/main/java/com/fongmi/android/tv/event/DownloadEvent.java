package com.fongmi.android.tv.event;

import com.fongmi.android.tv.bean.DownloadMsg;

import org.greenrobot.eventbus.EventBus;

public class DownloadEvent {
    private Type type;
    private String msg;
    private DownloadMsg downloadMsg;

    public static void error(String msg) {
        EventBus.getDefault().post(new DownloadEvent(Type.SUCCESS,msg));
    }

    public static void success(String msg) {
        EventBus.getDefault().post(new DownloadEvent(Type.ERROR,msg));
    }

    public static void postDownloadMsg(DownloadMsg downloadMsg) {
        EventBus.getDefault().post(new DownloadEvent(downloadMsg));
    }

    private DownloadEvent(Type type,String msg,DownloadMsg downloadMsg) {
        this.type = type;
        this.msg = msg;
        this.downloadMsg = downloadMsg;
    }

    public DownloadEvent(Type type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public DownloadEvent(DownloadMsg downloadMsg) {
        this.downloadMsg = downloadMsg;
    }
    public String getMsg() {
        return msg;
    }
    public Type getType() {
        return type;
    }

    public DownloadMsg getDownloadMsg(){
        return downloadMsg;
    }
    public enum Type {
        SUCCESS, ERROR
    }
}