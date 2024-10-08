package com.fongmi.android.tv.bean;

import android.text.TextUtils;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Drm {

    @SerializedName("key")
    private String key;
    @SerializedName("type")
    private String type;

    public static Drm create(String key, String type) {
        return new Drm(key, type);
    }

    private Drm(String key, String type) {
        this.key = key;
        this.type = type;
    }

    private String getKey() {
        return TextUtils.isEmpty(key) ? "" : key;
    }

    private String getType() {
        return TextUtils.isEmpty(type) ? "" : type;
    }

    private UUID getUUID() {
        if (getType().contains("playready")) return C.PLAYREADY_UUID;
        if (getType().contains("widevine")) return C.WIDEVINE_UUID;
        if (getType().contains("clearkey")) return C.CLEARKEY_UUID;
        return C.UUID_NIL;
    }

    public MediaItem.DrmConfiguration get() {
        return new MediaItem.DrmConfiguration.Builder(getUUID()).setLicenseUri(getKey()).build();
    }
}
