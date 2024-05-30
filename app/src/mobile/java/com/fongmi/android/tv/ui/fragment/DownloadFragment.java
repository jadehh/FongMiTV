package com.fongmi.android.tv.ui.fragment;

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

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.databinding.FragmentDownloadManageBinding;
import com.fongmi.android.tv.player.Players;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.utils.ResUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends BaseFragment  {
    private FragmentDownloadManageBinding mBinding;

    private static final String TAG = Players.class.getSimpleName();

    private final List<Fragment> fragments = new ArrayList<Fragment>();
    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }
    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadManageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        fragments.add(new DownloadingFragment());
        fragments.add(new DownloadFinishFragment());
        mBinding.pager.setOffscreenPageLimit(2);
        mBinding.pager.setAdapter(new PageAdapter(getChildFragmentManager()));
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

        mBinding.settingIcon.setOnClickListener(this::test);
    }

    private void test(View view) {
        Logger.t(TAG).d("先使用测试按钮，创建一个正在下载的任务");
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

    class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(@NonNull FragmentManager fm) {
            super(fm);
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


