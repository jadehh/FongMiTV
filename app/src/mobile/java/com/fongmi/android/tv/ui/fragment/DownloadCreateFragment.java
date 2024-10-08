package com.fongmi.android.tv.ui.fragment;


import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.fongmi.android.tv.databinding.FragmentDownloadCreateBinding;
import com.fongmi.android.tv.model.SiteViewModel;
import com.fongmi.android.tv.player.DownloadSource;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.utils.Notify;
import com.permissionx.guolindev.PermissionX;


public class DownloadCreateFragment extends BaseFragment {
    private FragmentDownloadCreateBinding mBinding;
    private Observer<String> mObserveDownload;
    private SiteViewModel mViewModel;
    public static DownloadCreateFragment newInstance() {
        return new DownloadCreateFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setViewModel();
    }
    @Override
    protected void initView() {
        mObserveDownload = this::setDownload;
    }
    public void setDownload(String msg){
        Notify.dismiss();
        Notify.show(msg);
    }
    public void setViewModel(){
        mViewModel = new ViewModelProvider(this).get(SiteViewModel.class);
        mViewModel.download.observeForever(mObserveDownload);
        mViewModel.downloadEp.observe(this, url -> {
            Notify.progress(getActivity());
            DownloadSource.get().initDownload(getActivity());
            mViewModel.download(url);
        });
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
        PermissionX.init(getActivity()).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request((allGranted, grantedList, deniedList) ->  mViewModel.setDownload(url));
    }
}
