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
import com.fongmi.android.tv.databinding.FragmentDownloadFinishBinding;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.event.MessageEvent;
import com.fongmi.android.tv.ui.adapter.DownloadFinishAdapter;
import com.fongmi.android.tv.ui.adapter.DownloadingAdapter;
import com.fongmi.android.tv.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DownloadFinishFragment extends BaseFragment implements DownloadFinishAdapter.OnClickListener{

    private FragmentDownloadFinishBinding mBinding;

    private DownloadFinishAdapter mAdapter;

    private final List<DownloadTask> list = new ArrayList<>();

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
        Msg msg = event.getMessage();
        if (msg.getType() == Constant.DOWNLOAD_Success_MESSAGE_TYPE){
            refreshData( (List<DownloadTask>) msg.getObj());
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
        mBinding.recycler.setAdapter(mAdapter = new DownloadFinishAdapter(this,list));
    }


    @Override
    public void openFile(DownloadTask task) {

    }

    @Override
    public void deleTask(DownloadTask task) {
        DownloadSource.get().delete(task);
    }

    @Override
    public void refreshData(List<DownloadTask> tasks) {
        if (list.size() != tasks.size()){
            list.clear();
            list.addAll(tasks);
            mAdapter.notifyDataSetChanged();
        }
    }
}
