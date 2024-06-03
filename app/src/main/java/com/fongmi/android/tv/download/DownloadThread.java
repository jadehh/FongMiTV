package com.fongmi.android.tv.download;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.utils.DownloadingUtil;

import java.util.List;

public class DownloadThread extends AsyncTask<Void, List<DownloadTask>,List<DownloadTask>> {
    public DownloadThread(){

    }
    @Override
    protected List<DownloadTask> doInBackground(Void... objects) {
        while (DownloadingUtil.getInstance().isIsLoopDown()){
            DownloadUpdateUI.getInstance().downUpdate();
            SystemClock.sleep(1000);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List<DownloadTask>... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<DownloadTask> downloadTasks) {
        super.onPostExecute(downloadTasks);
    }
}
