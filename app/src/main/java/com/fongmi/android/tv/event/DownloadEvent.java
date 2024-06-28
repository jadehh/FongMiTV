package com.fongmi.android.tv.event;

import org.greenrobot.eventbus.EventBus;

public class DownloadEvent {

    private final Type type;

    private  final String msg;
    public static void error(String msg) {
        EventBus.getDefault().post(new DownloadEvent(Type.SUCCESS,msg));
    }

    public static void success(String msg) {
        EventBus.getDefault().post(new DownloadEvent(Type.ERROR,msg));
    }

    private DownloadEvent(Type type,String msg) {
        this.type = type;
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
    public Type getType() {
        return type;
    }

    public enum Type {
        SUCCESS, ERROR
    }
}
