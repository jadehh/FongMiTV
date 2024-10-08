package com.fongmi.android.tv.server.process;

import com.fongmi.android.tv.server.Nano;
import com.github.catvod.utils.Asset;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class Parse implements Process {

    @Override
    public boolean isRequest(NanoHTTPD.IHTTPSession session, String path) {
        return "/parse".equals(path);
    }

    @Override
    public NanoHTTPD.Response doResponse(NanoHTTPD.IHTTPSession session, String path, Map<String, String> files) {
        try {
            Map<String, String> params = session.getParms();
            String jxs = params.get("jxs");
            String url = params.get("url");
            String html = String.format(Asset.read("parse.html"), jxs, url);
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, html);
        } catch (Exception e) {
            return Nano.error(e.getMessage());
        }
    }
}
