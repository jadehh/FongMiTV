package com.fongmi.android.tv.ui.dialog;

import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.api.config.VodConfig;
import com.fongmi.android.tv.bean.Config;
import com.fongmi.android.tv.bean.Device;
import com.fongmi.android.tv.bean.History;
import com.fongmi.android.tv.bean.Keep;
import com.fongmi.android.tv.databinding.DialogDeviceBinding;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.ui.adapter.DeviceAdapter;
import com.fongmi.android.tv.ui.cast.ScanEvent;
import com.fongmi.android.tv.ui.cast.ScanTask;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.ResUtil;
import com.github.catvod.net.OkHttp;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SyncDialog implements DeviceAdapter.OnClickListener, ScanTask.Listener {

    private final FormBody.Builder body;
    private final OkHttpClient client;
    private DialogDeviceBinding binding;
    private DeviceAdapter adapter;
    private String type;

    private final AlertDialog dialog;


    public static SyncDialog create(FragmentActivity activity) {
        return new SyncDialog(activity);
    }

    public SyncDialog(FragmentActivity activity) {
        client = OkHttp.client(Constant.TIMEOUT_SYNC);
        this.binding = DialogDeviceBinding.inflate(LayoutInflater.from(activity));
        this.dialog = new MaterialAlertDialogBuilder(activity).setView(binding.getRoot()).create();
        body = new FormBody.Builder();
    }

    private void initDialog() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (ResUtil.getScreenWidth() * 0.4f);
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setDimAmount(0);
        dialog.show();
    }

    public SyncDialog history() {
        type = "history";
        body.add("device", Device.get().toString());
        body.add("targets", App.gson().toJson(History.get()));
        if (VodConfig.getUrl() != null) body.add("url", VodConfig.getUrl());
        return this;
    }

    public SyncDialog keep() {
        type = "keep";
        body.add("device", Device.get().toString());
        body.add("targets", App.gson().toJson(Keep.getVod()));
        body.add("configs", App.gson().toJson(Config.findUrls()));
        return this;
    }

    public void show() {
        initDialog();
        initView();
        initEvent();
    }


    protected void initView() {
        EventBus.getDefault().register(this);
        setRecyclerView();
        getDevice();
    }

    protected void initEvent() {
        binding.refresh.setOnClickListener(v -> onRefresh());
    }

    private void setRecyclerView() {
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter = new DeviceAdapter(this));
    }

    private void getDevice() {
        adapter.addAll(Device.getAll());
        if (adapter.getItemCount() == 0) App.post(this::onRefresh, 1000);
    }

    private void onRefresh() {
        ScanTask.create(this).start(adapter.getIps());
        adapter.clear();
    }

    private void onSuccess() {
        dismiss();
    }

    private void onError() {
        Notify.show(R.string.device_offline);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanEvent(ScanEvent event) {
        ScanTask.create(this).start(event.getAddress());
    }

    @Override
    public void onFind(List<Device> devices) {
        if (devices.size() > 0) adapter.addAll(devices);
    }

    @Override
    public void onItemClick(Device item) {
        OkHttp.newCall(client, item.getIp().concat("/action?do=sync&mode=0&type=").concat(type), body.build()).enqueue(getCallback());
    }

    @Override
    public boolean onLongClick(Device item) {
        OkHttp.newCall(client, item.getIp().concat("/action?do=sync&mode=1&type=").concat(type), body.build()).enqueue(getCallback());
        return true;
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                App.post(() -> onSuccess());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                App.post(() -> onError());
            }
        };
    }
    private void dismiss() {
        try {
            if (dialog != null) dialog.dismiss();
        } catch (Exception ignored) {
        }
    }
}