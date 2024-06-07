package com.fongmi.android.tv.impl;




import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.db.AppDatabase;

import java.util.List;

public class TaskModelImp {

    public List<DownloadTask> findLoadingTask() {
        return AppDatabase.get().getDownloadTaskDao().findLoadingTask(Constant.DOWNLOAD_SUCCESS);
    }

    public List<DownloadTask> findSuccessTask() {
        return AppDatabase.get().getDownloadTaskDao().findSuccessTask(Constant.DOWNLOAD_SUCCESS);
    }
}
