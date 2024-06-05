package com.fongmi.android.tv.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.fongmi.android.tv.bean.DownloadTask;

import java.util.List;


@Dao
public abstract class DownloadTaskDao extends BaseDao<DownloadTask> {
    @Query("SELECT * FROM DownloadTask WHERE url = :url ")
    public abstract List<DownloadTask> find(String url);

    @Query("SELECT * FROM DownloadTask WHERE parentId = :parentId ")
    public abstract List<DownloadTask> find(int parentId);

    @Query("SELECT * FROM DownloadTask WHERE taskStatus != :taskStatus  ORDER BY createTime ")
    public abstract List<DownloadTask> findLoadingTask(int taskStatus);

    @Query("SELECT * FROM DownloadTask WHERE taskStatus = :taskStatus1 OR taskStatus = :taskStatus2 And taskId=0")
    public abstract List<DownloadTask> findDowningTask(int taskStatus1,int taskStatus2);
}
