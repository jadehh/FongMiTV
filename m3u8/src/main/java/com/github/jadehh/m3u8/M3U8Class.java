package com.github.jadehh.m3u8;

import android.content.Context;
import android.os.SystemClock;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.m3u8.M3U8VodOption;
import com.arialyy.aria.core.processor.IBandWidthUrlConverter;
import com.arialyy.aria.core.processor.IVodTsUrlConverter;
import com.github.catvod.utils.Path;
import com.github.catvod.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class M3U8Class {
    private Context context;

    public M3U8Class(Context context){
        this.context = context;
    }


    public long startDownload(String url){
        long mTaskId = Aria.download(this.context)
                .load(url)
                .setFilePath(Path.m3u8().getAbsolutePath()+File.separator + Util.md5(url))
                .ignoreFilePathOccupy()
                .m3u8VodOption(getM3U8Option())
                .create();
        return mTaskId;
    }

    public M3U8Module getDownloadTask(long taskId){
        DownloadEntity downloadEntity = Aria.download(this.context).load(taskId).getEntity();
        return  new M3U8Module(downloadEntity.getState(),downloadEntity.getSpeed(),downloadEntity.getDownloadSize(),downloadEntity.getFileSize(),downloadEntity.getPercent(),downloadEntity.getFileName(),downloadEntity.getFilePath());
    }

    public void resumeDownload(long taskId){
        Aria.download(this.context).load(taskId).m3u8VodOption(getM3U8Option()).resume();
    }

    public void stopDownload(long taskId){
        Aria.download(this.context).load(taskId).stop();
    }

    public void deleteDownload(long taskId){
        Aria.download(this.context).load(taskId).cancel(true);
    }

    private M3U8VodOption getM3U8Option() {
        M3U8VodOption option = new M3U8VodOption();
        option.setUseDefConvert(false);
        option.generateIndexFile();
        option.setVodTsUrlConvert(new VodTsUrlConverter());
        option.setBandWidthUrlConverter(new BandWidthUrlConverter());
        return option;
    }

    static class VodTsUrlConverter implements IVodTsUrlConverter {
        @Override public List<String> convert(String m3u8Url, List<String> tsUrls) {
            int index = m3u8Url.lastIndexOf("/");
            List<String> convertedTsUrl = new ArrayList<>();
            String parentUrl = m3u8Url.substring(0, index + 1);
            for (String temp : tsUrls) {
                if (!temp.startsWith("http")){
                    convertedTsUrl.add(parentUrl + temp);
                }
                else convertedTsUrl.add(temp);
            }
            return convertedTsUrl;
        }
    }

    static class BandWidthUrlConverter implements IBandWidthUrlConverter {
        @Override public String convert(String m3u8Url, String bandWidthUrl) {
            int index = m3u8Url.lastIndexOf("/");
            return m3u8Url.substring(0, index + 1) + bandWidthUrl;
        }
    }
}