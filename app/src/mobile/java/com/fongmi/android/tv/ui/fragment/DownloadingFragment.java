package com.fongmi.android.tv.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.Msg;
import com.fongmi.android.tv.databinding.FragmentDownloadingBinding;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.event.MessageEvent;
import com.fongmi.android.tv.ui.adapter.DownloadingAdapter;
import com.fongmi.android.tv.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DownloadingFragment extends BaseFragment implements DownloadingAdapter.OnClickListener{

    FragmentDownloadingBinding mBinding;

    private final List<DownloadTask> list = new ArrayList<>();

    private DownloadingAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setItemAnimator(null);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycler.setAdapter(mAdapter = new DownloadingAdapter(this,list));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
        Msg msg = event.getMessage();
        if (msg.getType() == Constant.DOWNLOAD_UPDATE_MESSAGE_TYPE) refreshData( (List<DownloadTask>) msg.getObj());
    }

    @Override
    public void startTask(DownloadTask task) {
        DownloadSource.get().resumeDownload(task);
    }
    @Override
    public void stopTask(DownloadTask task) {
        DownloadSource.get().stopDownload(task);
    }

    @Override
    public void openFile(DownloadTask task) {

    }

    @Override
    public void deleTask(DownloadTask task) {

    }

    @Override
    public void refreshData(List<DownloadTask> tasks) {
        list.clear();
        list.addAll(tasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void alert(String msg, int msgType) {

    }
}
