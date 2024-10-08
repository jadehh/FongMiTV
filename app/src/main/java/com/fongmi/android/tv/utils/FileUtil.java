package com.fongmi.android.tv.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.impl.Callback;
import com.github.catvod.utils.Path;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static File getWall(int index) {
        return Path.files("wallpaper_" + index);
    }

    public static void openFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(getShareUri(file), FileUtil.getMimeType(file.getName()));
        App.get().startActivity(intent);
    }

    public static void zipFolder(File folder, File zip) {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zip));
            folderToZip("", folder, zipOut);
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void folderToZip(String parentPath, File folder, ZipOutputStream zipOut) throws Exception {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                folderToZip(parentPath + file.getName() + "/", file, zipOut);
                continue;
            }
            ZipEntry zipEntry = new ZipEntry(parentPath + file.getName());
            zipOut.putNextEntry(zipEntry);

            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
            }
            in.close();
        }
    }
    public static void extractGzip(File target, File path) {
        byte[] buffer = new byte[1024];
        try (GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(target))); BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(path))) {
            int read;
            while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractZip(File target, File path) {
        try (ZipFile zip = new ZipFile(target)) {
            Enumeration<?> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File out = new File(path, entry.getName());
                if (entry.isDirectory()) out.mkdirs();
                else Path.copy(zip.getInputStream(entry), out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearCache(Callback callback) {
        App.execute(() -> {
            Path.clear(Path.cache());
            if (callback != null) App.post(callback::success);
        });
    }

    public static void getCacheSize(Callback callback) {
        App.execute(() -> {
            String result = byteCountToDisplaySize(getFolderSize(Path.cache()));
            App.post(() -> callback.success(result));
        });
    }

    public static Uri getShareUri(String path) {
        return getShareUri(new File(path.replace("file://", "")));
    }

    public static Uri getShareUri(File file) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.N ? Uri.fromFile(file) : FileProvider.getUriForFile(App.get(), App.get().getPackageName() + ".provider", file);
    }

    private static String getMimeType(String fileName) {
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        return TextUtils.isEmpty(mimeType) ? "*/*" : mimeType;
    }

    private static long getFolderSize(File file) {
        long size = 0;
        if (file == null) return 0;
        if (file.isDirectory()) for (File f : Path.list(file)) size += getFolderSize(f);
        else size = file.length();
        return size;
    }

    private static String byteCountToDisplaySize(long size) {
        if (size <= 0) return "0 KB";
        String[] units = new String[]{"bytes", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
