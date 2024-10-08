package com.fongmi.android.tv.event;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.utils.ResUtil;

import org.greenrobot.eventbus.EventBus;

public class ErrorEvent {

    private final Type type;
    private final int retry;
    private String msg;
    private int code;

    public static void url(int retry) {
        EventBus.getDefault().post(new ErrorEvent(Type.URL, retry, -1));
    }

    public static void url(int retry, int code) {
        EventBus.getDefault().post(new ErrorEvent(Type.URL, retry, code));
    }

    public static void flag() {
        EventBus.getDefault().post(new ErrorEvent(Type.FLAG, 0, -1));
    }

    public static void parse() {
        EventBus.getDefault().post(new ErrorEvent(Type.PARSE, 0, -1));
    }

    public static void timeout() {
        EventBus.getDefault().post(new ErrorEvent(Type.TIMEOUT, 0, -1));
    }

    public static void extract(String msg) {
        EventBus.getDefault().post(new ErrorEvent(Type.EXTRACT, 0, -1, msg));
    }

    public ErrorEvent(Type type, int retry, int code) {
        this.type = type;
        this.retry = retry;
        this.code = code;
    }

    public ErrorEvent(Type type, int retry, int code, String msg) {
        this.msg = msg;
        this.type = type;
        this.retry = retry;
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public int getRetry() {
        return retry;
    }

    public boolean isUrl() {
        return Type.URL.equals(getType());
    }

    public int getCode() {
        return code;
    }

    public boolean isDecode() {
        return code / 1000 == 4;
    }

    public boolean isExo() {
        return code / 1000 == 2 || code / 1000 == 3 || code / 1000 == 4;
    }

    public String getMsg() {
        if (type == Type.URL) return ResUtil.getString(R.string.error_play_url, code);
        if (type == Type.FLAG) return ResUtil.getString(R.string.error_play_flag);
        if (type == Type.PARSE) return ResUtil.getString(R.string.error_play_parse);
        if (type == Type.TIMEOUT) return ResUtil.getString(R.string.error_play_timeout);
        return msg;
    }

    public enum Type {
        URL, FLAG, PARSE, TIMEOUT, EXTRACT
    }
}
