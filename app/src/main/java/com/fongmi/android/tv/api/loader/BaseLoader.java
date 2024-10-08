package com.fongmi.android.tv.api.loader;

import com.fongmi.android.tv.api.config.LiveConfig;
import com.fongmi.android.tv.api.config.VodConfig;
import com.fongmi.android.tv.bean.Live;
import com.fongmi.android.tv.bean.Site;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderNull;
import com.github.catvod.utils.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseLoader {

    private final JarLoader jarLoader;
    private final PyLoader pyLoader;
    private final JsLoader jsLoader;

    private static class Loader {
        static volatile BaseLoader INSTANCE = new BaseLoader();
    }

    public static BaseLoader get() {
        return Loader.INSTANCE;
    }

    private BaseLoader() {
        this.jarLoader = new JarLoader();
        this.pyLoader = new PyLoader();
        this.jsLoader = new JsLoader();
    }

    public void clear() {
        this.jarLoader.clear();
        this.pyLoader.clear();
        this.jsLoader.clear();
    }

    public Spider getSpider(String key, String api, String ext, String jar) {
        boolean js = api.contains(".js");
        boolean py = api.contains(".py");
        boolean csp = api.startsWith("csp_");
        if (py) return pyLoader.getSpider(key, api, ext);
        else if (js) return jsLoader.getSpider(key, api, ext);
        else if (csp) return jarLoader.getSpider(key, api, ext, jar);
        else return new SpiderNull();
    }

    public Spider getSpider(Map<String, String> params) {
        if (!params.containsKey("siteKey")) return new SpiderNull();
        Live live = LiveConfig.get().getLive(params.get("siteKey"));
        Site site = VodConfig.get().getSite(params.get("siteKey"));
        if (!site.isEmpty()) return site.spider();
        if (!live.isEmpty()) return live.spider();
        return new SpiderNull();
    }

    public void setRecent(String key, String api, String jar) {
        boolean js = api.contains(".js");
        boolean py = api.contains(".py");
        boolean csp = api.startsWith("csp_");
        if (js) jsLoader.setRecent(key);
        else if (py) pyLoader.setRecent(key);
        else if (csp) jarLoader.setRecent(jar);
    }

    public Object[] proxyLocal(Map<String, String> params) {
        if ("js".equals(params.get("do"))) {
            return jsLoader.proxyInvoke(params);
        } else if ("py".equals(params.get("do"))) {
            return pyLoader.proxyInvoke(params);
        } else {
            return jarLoader.proxyInvoke(params);
        }
    }

    public void parseJar(String jar) {
        jarLoader.parseJar(Util.md5(jar), jar);
    }

    public JSONObject jsonExt(String key, LinkedHashMap<String, String> jxs, String url) throws Throwable {
        return jarLoader.jsonExt(key, jxs, url);
    }

    public JSONObject jsonExtMix(String flag, String key, String name, LinkedHashMap<String, HashMap<String, String>> jxs, String url) throws Throwable {
        return jarLoader.jsonExtMix(flag, key, name, jxs, url);
    }
}
