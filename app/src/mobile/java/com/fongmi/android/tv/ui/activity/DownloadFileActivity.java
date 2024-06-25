package com.fongmi.android.tv.ui.activity;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.ActivityDownloadFileBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.ui.adapter.DownloadFinishAdapter;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.utils.Util;


import java.util.List;

public class DownloadFileActivity extends BaseActivity implements DownloadFinishAdapter.OnClickListener {

    private ActivityDownloadFileBinding mBinding;

    private DownloadFinishAdapter adapter;

    private  List<DownloadTask> list;

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityDownloadFileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
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
        adapter = new DownloadFinishAdapter(this,list);
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setItemAnimator(null);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycler.setAdapter(adapter);
    }


    @Override
    public void openFile(DownloadTask task) {
        //跳转到详情界面
        if (Util.getFileType(task.getFileName()) == 1) VideoActivity.file(this,task.getLocalPath());
    }

    @Override
    public void deleTask(DownloadTask task) {
        list.remove(task);
        DownloadSource.get().delete(task);
        refreshData(list);
    }

    @Override
    public void refreshData(List<DownloadTask> tasks) {
        adapter.notifyDataSetChanged();
    }
}
