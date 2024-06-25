package com.fongmi.android.tv.player;

import android.app.Activity;
import com.fongmi.android.tv.bean.Result;

public class Download{



    private static class Loader {
        static volatile Download INSTANCE = new Download();
    }

    public static Download get() {
        return Loader.INSTANCE;
    }

    public void title(String title) {

    }

    public Download result(Result result) {
        return this;
    }

    public void start(Activity activity) {

    }



}
