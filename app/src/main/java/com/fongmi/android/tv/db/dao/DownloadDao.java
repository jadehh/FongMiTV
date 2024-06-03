package com.fongmi.android.tv.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.fongmi.android.tv.bean.DownloadTask;

import java.util.List;


@Dao
public abstract class DownloadDao extends BaseDao<DownloadTask> {
    @Query("SELECT * FROM DownloadTask WHERE id = :id ")
    public abstract List<DownloadTask> find(int id);
}
