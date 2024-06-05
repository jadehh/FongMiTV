package com.fongmi.android.tv.utils;

public class TimeUtil {
    public static String formatFromSecond(int totalSecond)
    {
        int hour = totalSecond / 3600;
        int min = (totalSecond % 3600) / 60;
        int second = totalSecond % 60;

        String fortmatStr = "";
        if(hour > 0)
        {
            fortmatStr = String.format("%02d:%02d:%02d",hour, min, second);
        }
        else
        {
            fortmatStr = String.format("%02d:%02d", min, second);
        }

        return fortmatStr;
    }

}
