package com.fongmi.android.tv.player.extractor;

import android.os.SystemClock;

import com.fongmi.android.tv.player.Source;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Path;

public class ZLive implements Source.Extractor {

    private static final int PORT = 6677;
    private boolean init;

    private void init() {
        com.east.android.zlive.ZLive.INSTANCE.OnLiveStart(PORT);
        SystemClock.sleep(50);
        init = true;
    }

    @Override
    public boolean match(String scheme, String host) {
        return "zlive".equals(scheme);
    }

    @Override
    public String fetch(String url) throws Exception {
        if (!init) init();
        String[] split = url.split("/");
        OkHttp.newCall(String.format("http://127.0.0.1:%s/stream/open?uuid=%s", PORT, split[3])).execute();
        return String.format("http://127.0.0.1:%s/stream/live?uuid=%s&server=%s&group=5850&mac=00:00:00:00:00:00&dir=%s", PORT, split[3], split[2], Path.cache());
    }

    @Override
    public void stop() {
        try {
            if (init) com.east.android.zlive.ZLive.INSTANCE.OnLiveStop();
            SystemClock.sleep(50);
            init = false;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
    }
}
