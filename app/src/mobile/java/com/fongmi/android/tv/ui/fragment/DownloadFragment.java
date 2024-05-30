package com.fongmi.android.tv.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.databinding.FragmentDownloadManageBinding;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.quickjs.bean.Res;

public class DownloadFragment extends BaseFragment {
    private FragmentDownloadManageBinding mBinding;
    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }
    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadManageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        setPageView();
    }


    protected void setPageView(){
        mBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                changeTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    private void changeTab(int index) {
        if (index == 0) {
            mBinding.downloading.setTextColor(ResUtil.getColor(R.color.white));
            mBinding.downloadFinish.setTextColor(ResUtil.getColor(R.color.white_90));
        } else {
            mBinding.downloadFinish.setTextColor(getResources().getColor(R.color.white));
            mBinding.downloading.setTextColor(getResources().getColor(R.color.white_90));
        }
    }
}
