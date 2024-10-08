package com.fongmi.android.tv.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.db.AppDatabase;
import com.github.catvod.utils.Util;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity
public class Download {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private String id;
    @SerializedName("vodPic")
    private String vodPic;
    @SerializedName("vodName")
    private String vodName;
    @SerializedName("url")
    private String url;
    @SerializedName("header")
    private String header;
    @SerializedName("createTime")
    private long createTime;

    public static Download objectFrom(String str) {
        return App.gson().fromJson(str, Download.class);
    }

    public static List<Download> arrayFrom(String str) {
        Type listType = new TypeToken<List<Download>>() {}.getType();
        List<Download> items = App.gson().fromJson(str, listType);
        return items == null ? Collections.emptyList() : items;
    }

    public Download(String vodName, String vodPic, String url, String header) {
        this.id = Util.md5(url);
        this.vodName = vodName;
        this.vodPic = vodPic;
        this.url = url;
        this.header = header;
        setCreateTime(System.currentTimeMillis());
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getVodPic() {
        return vodPic;
    }

    public void setVodPic(String vodPic) {
        this.vodPic = vodPic;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static List<Download> get() {
        return AppDatabase.get().getDownloadDao().find();
    }

    public static void delete(String url) {
        AppDatabase.get().getDownloadDao().delete(Util.md5(url));
    }

    public static void delete(Download download) {
        AppDatabase.get().getDownloadDao().delete(download.getId());
    }

    public static void clear() {
        AppDatabase.get().getDownloadDao().delete();
    }

    public Download delete() {
        AppDatabase.get().getDownloadDao().delete(getId());
        return this;
    }

    public Download save() {
        AppDatabase.get().getDownloadDao().insertOrUpdate(this);
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return App.gson().toJson(this);
    }
}
