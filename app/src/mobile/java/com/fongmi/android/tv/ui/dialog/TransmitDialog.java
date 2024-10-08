package com.fongmi.android.tv.ui.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.api.config.VodConfig;
import com.fongmi.android.tv.bean.Device;
import com.fongmi.android.tv.event.ScanEvent;
import com.fongmi.android.tv.utils.ScanTask;
import com.fongmi.android.tv.databinding.DialogDeviceBinding;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.server.Server;
import com.fongmi.android.tv.ui.activity.ScanActivity;
import com.fongmi.android.tv.ui.adapter.DeviceAdapter;
import com.fongmi.android.tv.utils.Notify;
import com.github.catvod.net.OkHttp;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransmitDialog extends BaseDialog implements DeviceAdapter.OnClickListener, ScanTask.Listener {

    private RequestBody requestBody;
    private final OkHttpClient client;
    private DialogDeviceBinding binding;
    private DeviceAdapter adapter;
    private String type;

    public static TransmitDialog create() {
        return new TransmitDialog();
    }

    public TransmitDialog() {
        client = OkHttp.client(Constant.TIMEOUT_TRANSMIT);
    }

    public TransmitDialog apk(String path) {
        type = "apk";
        File file = new File(path);
        MediaType mediaType = MediaType.parse("multipart/form-data");
        MultipartBody.Builder body = new MultipartBody.Builder();
        body.setType(MultipartBody.FORM);
        body.addFormDataPart("name", file.getName());
        body.addFormDataPart("files-0", file.getName(), RequestBody.create(mediaType, file));
        requestBody = body.build();
        return this;
    }

    public TransmitDialog vodConfig() {
        type = "vod_config";
        FormBody.Builder body = new FormBody.Builder();
        if (VodConfig.getUrl() != null) body.add("url", VodConfig.getUrl());
        requestBody = body.build();
        return this;
    }

    public TransmitDialog wallConfig(String path) {
        type = "wall_config";
        File file = new File(path);
        MediaType mediaType = MediaType.parse("multipart/form-data");
        MultipartBody.Builder body = new MultipartBody.Builder();
        body.setType(MultipartBody.FORM);
        body.addFormDataPart("name", file.getName());
        body.addFormDataPart("files-0", file.getName(), RequestBody.create(mediaType, file));
        requestBody = body.build();
        return this;
    }

    public TransmitDialog pushRetore(String path) {
        type = "push_restore";
        File file = new File(path);
        MediaType mediaType = MediaType.parse("multipart/form-data");
        MultipartBody.Builder body = new MultipartBody.Builder();
        body.setType(MultipartBody.FORM);
        body.addFormDataPart("name", file.getName());
        body.addFormDataPart("files-0", file.getName(), RequestBody.create(mediaType, file));
        requestBody = body.build();
        return this;
    }

    public TransmitDialog pullRetore() {
        type = "pull_restore";
        FormBody.Builder body = new FormBody.Builder();
        body.add("ip", Server.get().getAddress());
        requestBody = body.build();
        return this;
    }

    public void show(Fragment fragment) {
        show(fragment.getActivity());
    }

    public void show(FragmentActivity activity) {
        for (Fragment f : activity.getSupportFragmentManager().getFragments()) if (f instanceof BottomSheetDialogFragment) return;
        show(activity.getSupportFragmentManager(), null);
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return binding = DialogDeviceBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setRecyclerView();
        getDevice();
    }

    @Override
    protected void initEvent() {
        binding.scan.setOnClickListener(v -> onScan());
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

    private void onScan() {
        ScanActivity.start(getActivity());
    }

    private void onSuccess() {
        dismiss();
        Notify.dismiss();
    }

    private void onError(Exception e) {
        Notify.show(e.getMessage());
        Notify.dismiss();
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
        Notify.progress(getContext());
        OkHttp.newCall(client, item.getIp().concat("/action?do=transmit&type=").concat(type), requestBody).enqueue(getCallback());
    }

    @Override
    public boolean onLongClick(Device item) {
        Notify.progress(getContext());
        OkHttp.newCall(client, item.getIp().concat("/action?do=transmit&type=").concat(type), requestBody).enqueue(getCallback());
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
                App.post(() -> onError(e));
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
