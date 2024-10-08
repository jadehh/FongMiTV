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
import com.fongmi.android.tv.bean.DownloadMsg;
import com.fongmi.android.tv.databinding.FragmentDownloadFinishBinding;
import com.fongmi.android.tv.event.DownloadEvent;
import com.fongmi.android.tv.player.DownloadSource;
import com.fongmi.android.tv.service.DownloadService;
import com.fongmi.android.tv.ui.adapter.DownloadAdapter;
import com.fongmi.android.tv.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DownloadFinishFragment extends BaseFragment implements DownloadAdapter.OnClickListener{

    private FragmentDownloadFinishBinding mBinding;

    private final OnClickListener mListener;

    private DownloadAdapter mAdapter;

    private final List<DownloadTask> list = new ArrayList<>();

    public DownloadFinishFragment(OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadFinishBinding.inflate(inflater, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEvent(DownloadEvent event) {
        DownloadMsg downloadMsg = event.getDownloadMsg();
        if (downloadMsg.getType() == Constant.DOWNLOAD_Success_MESSAGE_TYPE){
            refreshData( (List<DownloadTask>) downloadMsg.getObj());
        }
    }
    @Override
    protected void initView() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setItemAnimator(null);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycler.setAdapter(mAdapter = new DownloadAdapter(this,list));
    }

    @Override
    public void startTask(DownloadTask task) {

    }

    @Override
    public void stopTask(DownloadTask task) {

    }

    @Override
    public void openFile(DownloadTask task) {
        this.mListener.openFile(task);
    }

    @Override
    public void deleteTask(DownloadTask task) {
        DownloadSource.get().deleteTask(task);
        DownloadService.getInstance().refreshDownloadFinish();
    }

    @Override
    public void refreshData(List<DownloadTask> tasks) {
        list.clear();
        list.addAll(tasks);
        mAdapter.notifyDataSetChanged();
    }


    public interface OnClickListener {
        void openFile(DownloadTask task);
    }
}
