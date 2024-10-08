package com.fongmi.android.tv.player.extractor;

import com.fongmi.android.tv.player.Source;
import com.fongmi.android.tv.server.Server;

public class Proxy implements Source.Extractor {

    @Override
    public boolean match(String scheme, String host) {
        return "proxy".equals(scheme);
    }

    @Override
    public String fetch(String url) throws Exception {
        return url.replace("proxy://", Server.get().getAddress("/proxy?"));
    }

    @Override
    public void stop() {
    }

    @Override
    public void exit() {
    }
}
