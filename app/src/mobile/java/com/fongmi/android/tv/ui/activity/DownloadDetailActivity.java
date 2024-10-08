package com.fongmi.android.tv.ui.activity;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.bean.DownloadMsg;
import com.fongmi.android.tv.databinding.ActivityDownloadDetailBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.event.DownloadEvent;
import com.fongmi.android.tv.player.DownloadSource;
import com.fongmi.android.tv.service.DownloadService;
import com.fongmi.android.tv.ui.adapter.DownloadAdapter;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.utils.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DownloadDetailActivity extends BaseActivity implements DownloadAdapter.OnClickListener {
    private ActivityDownloadDetailBinding mBinding;
    private DownloadAdapter adapter;

    private  List<DownloadTask> list;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEvent(DownloadEvent event) {
        DownloadMsg msg = event.getDownloadMsg();
        if (msg.getType() == Constant.DOWNLOAD_UPDATE_MESSAGE_TYPE){
            refreshData(AppDatabase.get().getDownloadTaskDao().find(getTaskId()));
        }
    }

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityDownloadDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public int getTaskId() {
        return getIntent().getIntExtra("taskId",0);
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        setRecyclerView();
    }


    private void setRecyclerView() {
        list = AppDatabase.get().getDownloadTaskDao().find(getTaskId());
        adapter = new DownloadAdapter(this,list);
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setItemAnimator(null);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycler.setAdapter(adapter);
    }


    @Override
    public void startTask(DownloadTask task) {
        DownloadSource.get().resumeTask(task);
        DownloadService.getInstance().refreshDownloading();
    }

    @Override
    public void stopTask(DownloadTask task) {
        DownloadSource.get().stopTask(task,false);
        DownloadService.getInstance().refreshDownloading();
    }

    @Override
    public void openFile(DownloadTask task) {
        if (FileUtil.getFileType(task.getFileName()) == 1){
            if (task.getTaskType() == Constant.JIANPIAN_DOWNLOAD_TYPE) VideoActivity.start(this,task.getLocalPath());
            else VideoActivity.file(this,task.getLocalPath());
        }
    }

    @Override
    public void deleteTask(DownloadTask task) {
        list.remove(task);
        DownloadSource.get().deleteTask(task);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshData(List<DownloadTask> tasks) {
        list.clear();
        list.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

}