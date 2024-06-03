package com.fongmi.android.tv.download;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.Msg;
import com.fongmi.android.tv.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DownloadUpdateUI {
    private static DownloadUpdateUI downloadUpdateUI;
    private List<DownloadTask> tasks;

    public DownloadUpdateUI(){
        tasks=new ArrayList<>();
    }

    public static synchronized DownloadUpdateUI getInstance() {
        if (downloadUpdateUI == null) {
            downloadUpdateUI = new DownloadUpdateUI();
        }
        return downloadUpdateUI;
    }
    public void downUpdate(){

        EventBus.getDefault().postSticky(new MessageEvent(new Msg(Constant.DOWNLOAD_UPDATE_MESSAGE_TYPE, tasks)));

    }

}
