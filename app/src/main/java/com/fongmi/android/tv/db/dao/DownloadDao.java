package com.fongmi.android.tv.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.fongmi.android.tv.bean.Download;

import java.util.List;

@Dao
public abstract class DownloadDao extends BaseDao<Download> {

    @Query("SELECT * FROM Download ORDER BY createTime DESC")
    public abstract List<Download> find();
    @Query("SELECT * FROM Download WHERE id = :id ORDER BY createTime DESC")
    public abstract Download find(String id);

    @Query("DELETE FROM Download WHERE id = :id")
    public abstract void delete(String id);

    @Query("DELETE FROM Download")
    public abstract void delete();
}
