package com.fongmi.android.tv;

public class Constant {
    //快進時間單位
    public static final int INTERVAL_SEEK = 10 * 1000;
    //控件隱藏時間
    public static final int INTERVAL_HIDE = 5 * 1000;
    //網路偵測間隔
    public static final int INTERVAL_TRAFFIC = 500;
    //點播爬蟲時間
    public static final int TIMEOUT_VOD = 30 * 1000;
    //直播爬蟲時間
    public static final int TIMEOUT_LIVE = 30 * 1000;
    //節目爬蟲時間
    public static final int TIMEOUT_EPG = 5 * 1000;
    //播放超時時間
    public static final int TIMEOUT_PLAY = 15 * 1000;
    //解析預設時間
    public static final int TIMEOUT_PARSE_DEF = 15 * 1000;
    //嗅探超時時間
    public static final int TIMEOUT_PARSE_WEB = 15 * 1000;
    //直播解析時間
    public static final int TIMEOUT_PARSE_LIVE = 10 * 1000;
    //同步超時時間
    public static final int TIMEOUT_SYNC = 2 * 1000;
    //传送超時時間
    public static final int TIMEOUT_TRANSMIT = 60 * 1000;
    //搜尋線程數量
    public static final int THREAD_POOL = 5;

    //迅雷下载类型
    public static final int THUNDER_TYPE = 0;


    //0连接中 1下载中 2下载完成 3失败
    public static final int DOWNLOAD_CONNECTION = 0;
    public static final int DOWNLOAD_LOADING = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    public static final int DOWNLOAD_FAIL = 3;
    public static final int DOWNLOAD_STOP = 4;
    public static final int DOWNLOAD_WAIT = 5;

    //开启下载消息类型
    public static final int DOWNLOAD_START_MESSAGE_TYPE = 0;
    //改变下载消息类型
    public static final int DOWNLOAD_SWITCH_MESSAGE_TYPE = 1;

    //更新下载消息类型
    public static final int DOWNLOAD_UPDATE_MESSAGE_TYPE = 3;
}
