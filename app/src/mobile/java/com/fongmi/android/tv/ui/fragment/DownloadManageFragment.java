package com.fongmi.android.tv.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.FragmentDownloadManageBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.player.DownloadSource;
import com.fongmi.android.tv.service.DownloadService;
import com.fongmi.android.tv.ui.activity.DownloadDetailActivity;
import com.fongmi.android.tv.ui.activity.MainActivity;
import com.fongmi.android.tv.ui.activity.VideoActivity;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.utils.FileChooser;
import com.fongmi.android.tv.utils.FileUtil;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

import okio.Utf8;

public class DownloadManageFragment extends BaseFragment implements DownloadFinishFragment.OnClickListener, DownloadingFragment.OnClickListener{
    private FragmentDownloadManageBinding mBinding;

    private final List<Fragment> fragments = new ArrayList<>();

    public static DownloadManageFragment newInstance() {
        return new DownloadManageFragment();
    }
    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadManageBinding.inflate(inflater, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJianPianDownload();
    }


    private MainActivity getRoot() {
        return (MainActivity) getActivity();
    }


    @Override
    protected void initView() {
        fragments.add(new DownloadingFragment(this));
        fragments.add(new DownloadFinishFragment(this));
        mBinding.pager.setAdapter(new PageAdapter(getChildFragmentManager(), fragments));
        mBinding.downloadFinish.setTextColor(ResUtil.getColor(com.github.bassaer.library.R.color.grey_500));
    }

    @Override
    protected void initEvent() {
        mBinding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }
        });
        mBinding.downloading.setOnClickListener(v->clickTab(0));
        mBinding.downloadFinish.setOnClickListener(v->clickTab(1));
        mBinding.add.setOnClickListener(this::startDownloadUrl);

    }

    private void changeTab(int index) {
        if (index == 0) {
            mBinding.downloading.setTextColor(ResUtil.getColor(R.color.white));
            mBinding.downloadFinish.setTextColor(ResUtil.getColor(com.github.bassaer.library.R.color.grey_500));
        } else {
            mBinding.downloadFinish.setTextColor(getResources().getColor(R.color.white));
            mBinding.downloading.setTextColor(getResources().getColor(com.github.bassaer.library.R.color.grey_500));
        }
    }

    private void clickTab(int index) {
        mBinding.pager.arrowScroll(index+1);
        changeTab(index);
    }

    protected void initJianPianDownload(){
        Notify.progress(getActivity());
        App.execute(()-> DownloadSource.get().initDownload(getActivity(),new Callback() {
            @Override
            public void success() {
                Notify.dismiss();
                loadingDownloadService();
            }
        }));
    }

    protected void loadingDownloadService(){
        App.execute(()-> DownloadService.getInstance().updateDownloadService());
    }

    private void startDownloadUrl(View view) {
        getRoot().change(5);
    }

    @Override
    public void openFile(DownloadTask task) {
        if (task.getTaskStatus() == Constant.DOWNLOAD_SUCCESS){
            if (task.getFile()){
                Intent intent = new Intent(getRoot(), DownloadDetailActivity.class);
                intent.putExtra("taskId", task.getId());
                getRoot().startActivity(intent);
            }else{
                if (FileUtil.getFileType(task.getFileName()) == 1){
                    if (task.getTaskType() == Constant.JIANPIAN_DOWNLOAD_TYPE) {
                        VideoActivity.start(getActivity(),task.getUrl());
                    }
                    else {
                        VideoActivity.file(getActivity(),task.getLocalPath(),task.getFileName());
                    }
                }
            }
        }
    }

    class PageAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments;

        public PageAdapter(@NonNull FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        }
    }

}
