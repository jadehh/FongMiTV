package com.fongmi.android.tv.utils;

import android.os.Build;
import android.os.Environment;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.impl.X5WebViewCallback;
import com.fongmi.android.tv.server.Server;
import com.github.catvod.utils.Path;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsCommonCode;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.export.external.TbsCoreSettings;

import java.io.File;
import java.util.HashMap;

public class Tbs {
    private static final String TAG = Tbs.class.getSimpleName();

    private static boolean isCpu64Bit() {
        for (String abi : Build.SUPPORTED_ABIS) {
            if (abi.contains("64")) return true;
        }
        return false;
    }

    public static String getUrl() {
        File file = new File(Path.tv(), "x5.tbs.apk");
        if (file.exists()) return Server.get().getAddress("/file/TV/x5.tbs.apk");
        File x5 = new File(Path.download(), "x5.tbs.apk");
        if (x5.exists()) return Server.get().getAddress("/file/"+ Environment.DIRECTORY_DOWNLOADS +"/x5.tbs.apk");
        return Server.get().getAddress("/x5.tbs.apk");
    }

    private static void tbsInit() {
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        TbsDownloader.stopDownload();
        QbSdk.PreInitCallback callback = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean finished) {
                if (finished) Notify.show(R.string.x5webview_enabled);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        QbSdk.initX5Environment(App.get(), callback);
    }

    public static void init() {
        if (Setting.getParseWebView() == 0) return;
        if (QbSdk.isTbsCoreInited()) return;
        App.post(() -> tbsInit());
    }

    public static String url() {
        return getUrl();
    }

    public static File file() {
        File file = Path.cache("x5.tbs.apk");
        return file;
    }

    public static void remove() {
        File file = file();
        if (file.exists()) file.delete();
    }

    public static void install(X5WebViewCallback callback) {
        boolean canLoadX5 = QbSdk.canLoadX5(App.get());
        if (canLoadX5) return;
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        TbsListener tbsListener = new TbsListener() {

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onDownloadFinish(int stateCode) {
                Logger.t(TAG).d("onDownloadFinish:" + stateCode);
            }

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onInstallFinish(int stateCode) {
                Logger.t(TAG).d("onInstallFinish:" + stateCode);
                if (stateCode == TbsCommonCode.INSTALL_SUCCESS) callback.onX5Success();
                else callback.onX5Error();
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            @Override
            public void onDownloadProgress(int progress) {
                Logger.t(TAG).d("onDownloadProgress:" + progress);
            }
        };
        QbSdk.setTbsListener(tbsListener);
        int version = isCpu64Bit() ? 46279 : 46914;
        QbSdk.reset(App.get());
        QbSdk.installLocalTbsCore(App.get(), version, file().getAbsolutePath());
    }

}
