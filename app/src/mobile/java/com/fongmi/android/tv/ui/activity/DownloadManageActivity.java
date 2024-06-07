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

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.databinding.ActivityDownloadManageBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.event.ErrorEvent;
import com.fongmi.android.tv.server.DownloadingService;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.ui.fragment.DownloadFinishFragment;
import com.fongmi.android.tv.ui.fragment.DownloadingFragment;
import com.fongmi.android.tv.utils.Notify;
import com.fongmi.android.tv.utils.ResUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DownloadManageActivity extends BaseActivity {
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
        Intent intent = new Intent(this, DownloadingService.class);
        startService(intent);
    }

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityDownloadManageBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        fragments.add(new DownloadingFragment());
        fragments.add(new DownloadFinishFragment());
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
        mBinding.settingIcon.setOnClickListener(this::test);
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

    private void test(View view) {
        Logger.t(TAG).d("先使用测试按钮，创建一个正在下载的任务");
        String url1 = "magnet:?xt=urn:btih:ff33292e9f14f4a49f6ee996e91672dfa1735937&dn=[www.domp4.cc]我叫白小飞.EP08.HD1080p.mp4&tr=https://tracker.iriseden.fr:443/announce&tr=https://tr.highstar.shop:443/announce&tr=https://tr.fuckbitcoin.xyz:443/announce&tr=https://tr.doogh.club:443/announce&tr=https://tr.burnabyhighstar.com:443/announce&tr=https://t.btcland.xyz:443/announce&tr=http://vps02.net.orel.ru:80/announce&tr=https://tracker.kuroy.me:443/announce&tr=http://tr.cili001.com:8070/announce&tr=http://t.overflow.biz:6969/announce&tr=http://t.nyaatracker.com:80/announce&tr=http://open.acgnxtracker.com:80/announce&tr=http://nyaa.tracker.wf:7777/announce&tr=http://home.yxgz.vip:6969/announce&tr=http://buny.uk:6969/announce&tr=https://tracker.tamersunion.org:443/announce&tr=https://tracker.nanoha.org:443/announce&tr=https://tracker.loligirl.cn:443/announce&tr=udp://bubu.mapfactor.com:6969/announce&tr=http://share.camoe.cn:8080/announce&tr=udp://movies.zsw.ca:6969/announce&tr=udp://ipv4.tracker.harry.lu:80/announce&tr=udp://tracker.sylphix.com:6969/announce&tr=http://95.216.22.207:9001/announce";
        String url2 = "https://download-cdn.jetbrains.com/idea/ideaIU-2024.1.2.exe?_ga=2.103997184.1095914730.1716882988-886861707.1704695179&_gl=1*1ij1pqk*_ga*ODg2ODYxNzA3LjE3MDQ2OTUxNzk.*_ga_9J976DJZ68*MTcxNjg4Mjk4Ny40My4xLjE3MTY4ODU0MzEuNjAuMC4w";
        String url3 = "https://down.sandai.net/thunder11/XunLeiWebSetup12.0.12.2510xl11.exe";
        if (AppDatabase.get().getDownloadTaskDao().find(url1).size() > 0){
            Notify.show("任务添加失败");
        }else{
            DownloadSource.get().startDownload(url1);
            Notify.show("任务添加成功");
        }
        if (AppDatabase.get().getDownloadTaskDao().find(url2).size() > 0){
            Notify.show("任务添加失败");
        }else{
            DownloadSource.get().startDownload(url2);
            Notify.show("任务添加成功");
        }
        if (AppDatabase.get().getDownloadTaskDao().find(url3).size() > 0){
            Notify.show("任务添加失败");
        }else{
            DownloadSource.get().startDownload(url3);
            Notify.show("任务添加成功");
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


