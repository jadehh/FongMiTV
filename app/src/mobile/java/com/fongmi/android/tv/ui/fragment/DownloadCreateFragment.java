package com.fongmi.android.tv.ui.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.bean.Download;
import com.fongmi.android.tv.databinding.FragmentDownloadCreateBinding;
import com.fongmi.android.tv.download.DownloadSource;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.utils.Notify;


import java.util.ArrayList;
import java.util.List;


public class DownloadCreateFragment extends BaseFragment {

    private FragmentDownloadCreateBinding mBinding;

    public static DownloadCreateFragment newInstance() {
        return new DownloadCreateFragment();
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadCreateBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEvent() {
        mBinding.startDownload.setOnClickListener(this::startDownloadClick);
    }

    private void startDownloadClick(View view) {
        String url = mBinding.downloadUrlInput.getText().toString().trim();
        Notify.progress(getActivity());
        String url1 = "magnet:?xt=urn:btih:ff33292e9f14f4a49f6ee996e91672dfa1735937&dn=[www.domp4.cc]我叫白小飞.EP08.HD1080p.mp4&tr=https://tracker.iriseden.fr:443/announce&tr=https://tr.highstar.shop:443/announce&tr=https://tr.fuckbitcoin.xyz:443/announce&tr=https://tr.doogh.club:443/announce&tr=https://tr.burnabyhighstar.com:443/announce&tr=https://t.btcland.xyz:443/announce&tr=http://vps02.net.orel.ru:80/announce&tr=https://tracker.kuroy.me:443/announce&tr=http://tr.cili001.com:8070/announce&tr=http://t.overflow.biz:6969/announce&tr=http://t.nyaatracker.com:80/announce&tr=http://open.acgnxtracker.com:80/announce&tr=http://nyaa.tracker.wf:7777/announce&tr=http://home.yxgz.vip:6969/announce&tr=http://buny.uk:6969/announce&tr=https://tracker.tamersunion.org:443/announce&tr=https://tracker.nanoha.org:443/announce&tr=https://tracker.loligirl.cn:443/announce&tr=udp://bubu.mapfactor.com:6969/announce&tr=http://share.camoe.cn:8080/announce&tr=udp://movies.zsw.ca:6969/announce&tr=udp://ipv4.tracker.harry.lu:80/announce&tr=udp://tracker.sylphix.com:6969/announce&tr=http://95.216.22.207:9001/announce";
        String url2 = "https://download-cdn.jetbrains.com/idea/ideaIU-2024.1.2.exe?_ga=2.103997184.1095914730.1716882988-886861707.1704695179&_gl=1*1ij1pqk*_ga*ODg2ODYxNzA3LjE3MDQ2OTUxNzk.*_ga_9J976DJZ68*MTcxNjg4Mjk4Ny40My4xLjE3MTY4ODU0MzEuNjAuMC4w";
        String url3 = "https://down.sandai.net/thunder11/XunLeiWebSetup12.0.12.2510xl11.exe";
        String url4 = "ftp://a.gbl.114s.com:20320/1189/庆余年第二季35v2.mp4";
        String url5 = "ftp://a.gbl.114s.com:20320/0997/庆余年第二季01v2.mp4";
        String url6 = "https://gh.ddlc.top/https://github.com/jadehh/jadehh_file/releases/download/StreamServerV1.4.7/StreamServer_setup-V1.4-7.exe";
        String url7 = "https://gh.ddlc.top/https://github.com/jadehh/jadehh_file/releases/download/StreamServerV1.4.7/StreamServer-Linux-V1.4-7.zip";
        List<Download> downloads = new ArrayList<>();
        Download download1 = new Download();
        download1.setDownloadName("StreamServer_setup-V1.4-7.exe");
        download1.setDownloadUrl(url6);
        downloads.add(download1);
        Download download2 = new Download();
        download2.setDownloadName("StreamServer-Linux-V1.4-7.zip");
        download2.setDownloadUrl(url7);
        downloads.add(download2);
        App.execute(() -> DownloadSource.get().startTask("", url));
//        App.execute(() -> DownloadSource.get().startTask("工具集", downloads));
    }
}
