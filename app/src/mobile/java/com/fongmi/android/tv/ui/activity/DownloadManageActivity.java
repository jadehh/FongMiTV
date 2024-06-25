package com.fongmi.android.tv.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.ActivityDownloadManageBinding;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.impl.Callback;
import com.fongmi.android.tv.server.DownloadingService;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.ui.fragment.DownloadFinishFragment;
import com.fongmi.android.tv.ui.fragment.DownloadingFragment;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DownloadManageActivity extends BaseActivity implements DownloadFinishFragment.OnClickListener{
    private static final String TAG = DownloadManageActivity.class.getSimpleName();
    private final List<Fragment> fragments = new ArrayList<Fragment>();
    private ActivityDownloadManageBinding mBinding;

    public static DownloadManageActivity newInstance() {
        return new DownloadManageActivity();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, DownloadManageActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJianpian();
    }

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityDownloadManageBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        fragments.add(new DownloadingFragment());
        fragments.add(new DownloadFinishFragment(this));
        mBinding.pager.setAdapter(new PageAdapter(getSupportFragmentManager(), fragments));
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
        mBinding.settingIcon.setOnClickListener(this::startDownloadUrl);
    }

    protected void initJianpian(){
        Notify.progress(getActivity());
        App.execute(()->DownloadSource.get().addJianpianExtractor(new Callback() {
            @Override
            public void success() {
                Notify.dismiss();
                Intent intent = new Intent(getActivity(), DownloadingService.class);
                startService(intent);
            }
        }));
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
        if (index == 0) {
            mBinding.downloading.setTextColor(ResUtil.getColor(R.color.white));
            mBinding.downloadFinish.setTextColor(ResUtil.getColor(com.github.bassaer.library.R.color.grey_500));
        } else {
            mBinding.downloadFinish.setTextColor(getResources().getColor(R.color.white));
            mBinding.downloading.setTextColor(getResources().getColor(com.github.bassaer.library.R.color.grey_500));
        }
    }

    private void startDownloadUrl(View view) {
        Intent intent = new Intent(this, DownloadUrlActivity.class);
        startActivity(intent);
    }

    @Override
    public void openFile(DownloadTask task) {
        if (task.getFile()){
            Intent intent = new Intent(this, DownloadFileActivity.class);
            intent.putExtra("taskId", task.getId());
            startActivity(intent);
        }else{
            if (Util.getFileType(task.getFileName()) == 1){
                if (task.getTaskType() == Constant.JIANPIAN_TYPE){
                    VideoActivity.start(this,task.getLocalPath());
                }else{
                    VideoActivity.file(this,task.getLocalPath());
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


