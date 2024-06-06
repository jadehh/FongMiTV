package com.github.catvod.net.interceptor;

import androidx.annotation.NonNull;

import com.github.catvod.Proxy;
import com.github.catvod.utils.Util;
import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        URI uri = request.url().uri();
        String url = request.url().toString();
        Request.Builder builder = request.newBuilder();
        boolean local = url.contains(":" + Proxy.getPort() + "/");
        if (url.contains("+") && local) builder.url(url.replace("+", "%2B"));
        if (url.contains("gitcode.net")) builder.header(HttpHeaders.USER_AGENT, Util.CHROME);
        if (uri.getUserInfo() != null) builder.header(HttpHeaders.AUTHORIZATION, Util.basic(uri.getUserInfo()));
        return chain.proceed(builder.build());
    }
}
