package com.fongmi.android.tv.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.fongmi.android.tv.databinding.DialogX5webviewBinding;
import com.fongmi.android.tv.impl.X5WebViewCallback;
import com.fongmi.android.tv.utils.Download;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.Tbs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.util.Locale;

public class X5WebViewDialog  implements Download.Callback {

    private DialogX5webviewBinding binding;
    private AlertDialog dialog;
    private Activity activity;
    private X5WebViewCallback callback;
    private boolean confirm;

    public X5WebViewDialog(Activity activity) {
        this.activity = activity;
        this.confirm = false;
        this.callback = (X5WebViewCallback) activity;
    }

    public void show() {
        binding = DialogX5webviewBinding.inflate(LayoutInflater.from(activity));
        binding.confirm.setOnClickListener(this::confirm);
        binding.cancel.setOnClickListener(this::cancel);
        dialog = new MaterialAlertDialogBuilder(activity).setView(binding.getRoot()).setCancelable(false).create();
        dialog.show();
    }

    public static X5WebViewDialog create(Activity activity) {
        return new X5WebViewDialog(activity);
    }

    private String getTbs() {
        return Tbs.url();
    }

    private File getFile() {
        return Tbs.file();
    }

    private void cancel(View view) {
        dismiss();
        callback.onX5Cancel();
    }

    private void confirm(View view) {
        if (confirm) return;
        confirm = true;
        binding.confirm.setEnabled(false);
        Tbs.remove();
        Download.create(getTbs(), getFile(), this).start();
    }

    private void dismiss() {
        try {
            if (dialog != null) dialog.dismiss();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void progress(int progress) {
        binding.confirm.setText(String.format(Locale.getDefault(), "%1$d%%", progress));
    }

    @Override
    public void error(String msg) {
        Notify.show(msg);
        dismiss();
        callback.onX5Error();
    }

    @Override
    public void success(File file) {
        dismiss();
        Tbs.install(callback);
    }

}
