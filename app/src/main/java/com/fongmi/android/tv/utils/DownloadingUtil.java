package com.fongmi.android.tv.utils;

public class DownloadingUtil {
    private static DownloadingUtil downloadingUtil = null;

    private static boolean isLoopDown = true;


    public static synchronized DownloadingUtil getInstance() {
        if (downloadingUtil == null) {
            downloadingUtil = new DownloadingUtil();
        }
        return downloadingUtil;
    }

    public boolean isIsLoopDown() {
        return isLoopDown;
    }


}
