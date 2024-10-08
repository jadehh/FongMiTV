package com.fongmi.android.tv;


import android.content.Intent;
import android.provider.Settings;

import com.fongmi.android.tv.player.Players;
import com.fongmi.android.tv.utils.LanguageUtil;
import com.github.catvod.utils.Prefers;

public class Setting {

    public static String getDoh() {
        return Prefers.getString("doh");
    }

    public static void putDoh(String doh) {
        Prefers.put("doh", doh);
    }

    public static String getProxy() {
        return Prefers.getString("proxy");
    }

    public static void putProxy(String proxy) {
        Prefers.put("proxy", proxy);
    }

    public static String getKeep() {
        return Prefers.getString("keep");
    }

    public static void putKeep(String keep) {
        Prefers.put("keep", keep);
    }

    public static String getKeyword() {
        return Prefers.getString("keyword");
    }

    public static void putKeyword(String keyword) {
        Prefers.put("keyword", keyword);
    }

    public static String getHot() {
        return Prefers.getString("hot");
    }

    public static void putHot(String hot) {
        Prefers.put("hot", hot);
    }

    public static String getUa() {
        return Prefers.getString("ua");
    }

    public static void putUa(String ua) {
        Prefers.put("ua", ua);
    }

    public static int getWall() {
        return Prefers.getInt("wall", 1);
    }

    public static void putWall(int wall) {
        Prefers.put("wall", wall);
    }

    public static int getReset() {
        return Prefers.getInt("reset", 0);
    }

    public static void putReset(int reset) {
        Prefers.put("reset", reset);
    }

    public static int getPlayer() {
        return Prefers.getInt("player", Players.EXO);
    }

    public static void putPlayer(int player) {
        Prefers.put("player", player);
    }

    public static int getLivePlayer() {
        return Prefers.getInt("player_live", getPlayer());
    }

    public static void putLivePlayer(int player) {
        Prefers.put("player_live", player);
    }

    public static int getDecode(int player) {
        return Prefers.getInt("decode_" + player, Players.HARD);
    }

    public static void putDecode(int player, int decode) {
        Prefers.put("decode_" + player, decode);
    }

    public static int getRender() {
        return Prefers.getInt("render", 0);
    }

    public static void putRender(int render) {
        Prefers.put("render", render);
    }

    public static int getQuality() {
        return Prefers.getInt("quality", 2);
    }

    public static void putQuality(int quality) {
        Prefers.put("quality", quality);
    }

    public static int getSize() {
        return Prefers.getInt("size", 2);
    }

    public static void putSize(int size) {
        Prefers.put("size", size);
    }

    public static int getViewType(int viewType) {
        return Prefers.getInt("viewType", viewType);
    }

    public static void putViewType(int viewType) {
        Prefers.put("viewType", viewType);
    }

    public static int getScale() {
        return Prefers.getInt("scale");
    }

    public static void putScale(int scale) {
        Prefers.put("scale", scale);
    }

    public static int getLiveScale() {
        return Prefers.getInt("scale_live", getScale());
    }

    public static void putLiveScale(int scale) {
        Prefers.put("scale_live", scale);
    }

    public static int getHttp() {
        return Prefers.getInt("exo_http", 1);
    }

    public static void putHttp(int http) {
        Prefers.put("exo_http", http);
    }

    public static int getBuffer() {
        return Math.min(Math.max(Prefers.getInt("exo_buffer"), 1), 15);
    }

    public static void putBuffer(int buffer) {
        Prefers.put("exo_buffer", buffer);
    }

    public static int getFlag() {
        return Prefers.getInt("flag");
    }

    public static void putFlag(int flag) {
        Prefers.put("flag", flag);
    }

    public static int getEpisode() {
        return Prefers.getInt("episode");
    }

    public static void putEpisode(int episode) {
        Prefers.put("episode", episode);
    }

    public static int getBackground() {
        return Prefers.getInt("background", 2);
    }

    public static void putBackground(int background) {
        Prefers.put("background", background);
    }

    public static int getRtsp() {
        return Prefers.getInt("rtsp");
    }

    public static void putRtsp(int rtsp) {
        Prefers.put("rtsp", rtsp);
    }

    public static int getSiteMode() {
        return Prefers.getInt("site_mode", 1);
    }

    public static void putSiteMode(int mode) {
        Prefers.put("site_mode", mode);
    }

    public static int getSyncMode() {
        return Prefers.getInt("sync_mode");
    }

    public static void putSyncMode(int mode) {
        Prefers.put("sync_mode", mode);
    }

    public static boolean isBootLive() {
        return Prefers.getBoolean("boot_live");
    }

    public static void putBootLive(boolean boot) {
        Prefers.put("boot_live", boot);
    }

    public static boolean isInvert() {
        return Prefers.getBoolean("invert");
    }

    public static void putInvert(boolean invert) {
        Prefers.put("invert", invert);
    }

    public static boolean isAcross() {
        return Prefers.getBoolean("across", true);
    }

    public static void putAcross(boolean across) {
        Prefers.put("across", across);
    }

    public static boolean isChange() {
        return Prefers.getBoolean("change", true);
    }

    public static void putChange(boolean change) {
        Prefers.put("change", change);
    }

    public static boolean getUpdate() {
        return Prefers.getBoolean("update", true);
    }

    public static void putUpdate(boolean update) {
        Prefers.put("update", update);
    }

    public static boolean isPlayWithOthers() {
        return Prefers.getBoolean("play_with_others", false);
    }

    public static void putPlayWithOthers(boolean play) {
        Prefers.put("play_with_others", play);
    }

    public static boolean isDanmu() {
        return Prefers.getBoolean("danmu");
    }

    public static void putDanmu(boolean danmu) {
        Prefers.put("danmu", danmu);
    }

    public static boolean isDanmuLoad() {
        return Prefers.getBoolean("danmu_load", true);
    }

    public static void putDanmuLoad(boolean load) {
        Prefers.put("danmu_load", load);
    }

    public static int getDanmuSpeed() {
        return Math.min(Math.max(Prefers.getInt("danmu_speed", 2), 0), 3);
    }

    public static void putDanmuSpeed(int speed) {
        Prefers.put("danmu_speed", speed);
    }

    public static float getDanmuSize() {
        return Math.min(Math.max(Prefers.getFloat("danmu_size", 1.0f), 0.6f), 2.0f);
    }

    public static void putDanmuSize(float size) {
        Prefers.put("danmu_size", size);
    }

    public static int getDanmuLine(int line) {
        return Math.min(Math.max(Prefers.getInt("danmu_line", line), 1), 15);
    }

    public static void putDanmuLine(int line) {
        Prefers.put("danmu_line", line);
    }

    public static int getDanmuAlpha() {
        return Math.min(Math.max(Prefers.getInt("danmu_alpha", 90), 10), 100);
    }

    public static void putDanmuAlpha(int alpha) {
        Prefers.put("danmu_alpha", alpha);
    }

    public static boolean isCaption() {
        return Prefers.getBoolean("caption");
    }

    public static void putCaption(boolean caption) {
        Prefers.put("caption", caption);
    }

    public static boolean isTunnel() {
        return Prefers.getBoolean("exo_tunnel");
    }

    public static void putTunnel(boolean tunnel) {
        Prefers.put("exo_tunnel", tunnel);
    }

    public static int getBackupMode() {
        return Prefers.getInt("backup_mode", 1);
    }

    public static void putBackupMode(int auto) {
        Prefers.put("backup_mode", auto);
    }

    public static boolean isZhuyin() {
        return Prefers.getBoolean("zhuyin");
    }

    public static void putZhuyin(boolean zhuyin) {
        Prefers.put("zhuyin", zhuyin);
    }

    public static float getSubtitleTextSize() {
        return Prefers.getFloat("subtitle_text_size");
    }

    public static void putSubtitleTextSize(float value) {
        Prefers.put("subtitle_text_size", value);
    }

    public static float getSubtitleBottomPadding() {
        return Prefers.getFloat("subtitle_bottom_padding");
    }

    public static void putSubtitleBottomPadding(float value) {
        Prefers.put("subtitle_bottom_padding", value);
    }

    public static float getThumbnail() {
        return 0.3f * getQuality() + 0.4f;
    }

    public static boolean isBackgroundOff() {
        return getBackground() == 0;
    }

    public static boolean isBackgroundOn() {
        return getBackground() == 1 || getBackground() == 2;
    }

    public static boolean isBackgroundPiP() {
        return getBackground() == 2;
    }

    public static boolean hasCaption() {
        return new Intent(Settings.ACTION_CAPTIONING_SETTINGS).resolveActivity(App.get().getPackageManager()) != null;
    }

    public static boolean isDisplayTime() {
        return Prefers.getBoolean("display_time", false);
    }

    public static void putDisplayTime(boolean display) {
        Prefers.put("display_time", display);
    }

    public static boolean isDisplaySpeed() {
        return Prefers.getBoolean("display_speed", false);
    }

    public static void putDisplaySpeed(boolean display) {
        Prefers.put("display_speed", display);
    }

    public static boolean isDisplayDuration() {
        return Prefers.getBoolean("display_duration", false);
    }

    public static void putDisplayDuration(boolean display) {
        Prefers.put("display_duration", display);
    }

    public static boolean isDisplayMiniProgress() {
        return Prefers.getBoolean("display_mini_progress", false);
    }

    public static void putDisplayMiniProgress(boolean display) {
        Prefers.put("display_mini_progress", display);
    }

    public static boolean isDisplayVideoTitle() {
        return Prefers.getBoolean("display_video_title", false);
    }

    public static void putDisplayVideoTitle(boolean display) {
        Prefers.put("display_video_title", display);
    }

    public static float getPlaySpeed() {
        return Prefers.getFloat("play_speed", 1.0f);
    }

    public static void putPlaySpeed(float speed) {
        Prefers.put("play_speed", speed);
    }

    public static void putFullscreenMenuKey(int key) {
        Prefers.put("fullscreen_menu_key", key);
    }

    public static int getFullscreenMenuKey() {
        return Prefers.getInt("fullscreen_menu_key", 0);
    }

    public static void putHomeMenuKey(int key) {
        Prefers.put("home_menu_key", key);
    }

    public static int getHomeMenuKey() {
        return Prefers.getInt("home_menu_key", 0);
    }

    public static boolean isHomeSiteLock() {
        return Prefers.getBoolean("home_site_lock", false);
    }

    public static void putHomeSiteLock(boolean lock) {
        Prefers.put("home_site_lock", lock);
    }

    public static boolean isIncognito() {
        return Prefers.getBoolean("incognito");
    }

    public static void putIncognito(boolean incognito) {
        Prefers.put("incognito", incognito);
    }

    public static void putSmallWindowBackKey(int key) {
        Prefers.put("small_window_back_key", key);
    }

    public static int getSmallWindowBackKey() {
        return Prefers.getInt("small_window_back_key", 0);
    }

    public static void putHomeDisplayName(boolean change) {
        Prefers.put("home_display_name", change);
    }

    public static boolean isHomeDisplayName() {
        return Prefers.getBoolean("home_display_name", false);
    }

    public static boolean isAggregatedSearch() {
        return Prefers.getBoolean("aggregated_search", false);
    }

    public static void putAggregatedSearch(boolean search) {
        Prefers.put("aggregated_search", search);
    }

    public static void putHomeUI(int key) {
        Prefers.put("home_ui", key);
    }

    public static int getHomeUI() {
        return Prefers.getInt("home_ui", 1);
    }

    public static void putHomeButtons(String buttons) {
        Prefers.put("home_buttons", buttons);
    }

    public static String getHomeButtons(String defaultValue) {
        return Prefers.getString("home_buttons", defaultValue);
    }

    public static void putHomeButtonsSorted(String buttons) {
        Prefers.put("home_buttons_sorted", buttons);
    }

    public static String getHomeButtonsSorted(String defaultValue) {
        return Prefers.getString("home_buttons_sorted", defaultValue);
    }

    public static boolean isHomeHistory() {
        return Prefers.getBoolean("home_history", true);
    }

    public static void putHomeHistory(boolean show) {
        Prefers.put("home_history", show);
    }

    public static void putConfigCache(int key) {
        Prefers.put("config_cache", key);
    }

    public static int getConfigCache() {
        return Math.min(Prefers.getInt("config_cache", 0), 2);
    }

    public static void putLanguage(int key) {
        Prefers.put("language", key);
    }

    public static int getLanguage() {
        return Prefers.getInt("language", LanguageUtil.locale());
    }

    public static void putParseWebView(int key) {
        Prefers.put("parse_webview", key);
    }

    public static int getParseWebView() {
        return Prefers.getInt("parse_webview", 0);
    }

    public static boolean isSiteSearch() {
        return Prefers.getBoolean("site_search", false);
    }

    public static void putSiteSearch(boolean search) {
        Prefers.put("site_search", search);
    }

    public static boolean isRemoveAd() {
        return Prefers.getBoolean("remove_ad", false);
    }

    public static void putRemoveAd(boolean remove) {
        Prefers.put("remove_ad", remove);
    }

    public static String getThunderCacheDir() {
        return Prefers.getString("thunder_cache_dir", "");
    }

    public static void putThunderCacheDir(String dir) {
        Prefers.put("thunder_cache_dir", dir);
    }

}
