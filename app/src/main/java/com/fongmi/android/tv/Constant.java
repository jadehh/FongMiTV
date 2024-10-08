package com.fongmi.android.tv;

public class Constant {
    //快進時間單位
    public static final int INTERVAL_SEEK = 10 * 1000;
    //控件隱藏時間
    public static final int INTERVAL_HIDE = 5 * 1000;
    //網路偵測間隔
    public static final int INTERVAL_TRAFFIC = 1000;
    //點播爬蟲時間
    public static final int TIMEOUT_VOD = 30 * 1000;
    //直播爬蟲時間
    public static final int TIMEOUT_LIVE = 30 * 1000;
    //節目爬蟲時間
    public static final int TIMEOUT_EPG = 5 * 1000;
    //節目爬蟲時間
    public static final int TIMEOUT_XML = 15 * 1000;
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
    public static final int THREAD_POOL = 10;
    
  
    public static final int DOWNLOAD_OUTER_TYPE = 1;

    public static final int DOWNLOAD_INNER_TYPE = 2;

    //下载类型
    public static final int THUNDER_DOWNLOAD_TYPE = 0; // 迅雷下载类型

    public static final int JIANPIAN_DOWNLOAD_TYPE = 1; // 荐片下载类型
    public static final int M3U8_DOWNLOAD_TYPE = 2; // M3U8下载类型




    public static final int CUSTOM_DOWNLOAD_TYPE = 3;  // 自定义下载类型


    public static final int DOWNLOAD_CONNECTION = 0; // 连接中
    public static final int DOWNLOAD_LOADING = 1;  // 下载中
    public static final int DOWNLOAD_SUCCESS = 2; //下载完成
    public static final int DOWNLOAD_FAIL = 3; //失败
    public static final int DOWNLOAD_STOP = 4; //停止
    public static final int DOWNLOAD_WAIT = 5; //等待


    //开启下载消息类型
    public static final int DOWNLOAD_UPDATE_MESSAGE_TYPE = 0; // 更新下载信息
    public static final int DOWNLOAD_Success_MESSAGE_TYPE = 1; // 更新完成下载
}
