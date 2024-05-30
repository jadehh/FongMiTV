package com.fongmi.android.tv.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.databinding.FragmentDownloadingBinding;
import com.fongmi.android.tv.ui.base.BaseFragment;

public class DownloadingFragment extends BaseFragment {

    FragmentDownloadingBinding mBinding;

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadingBinding.inflate(inflater, container, false);
    }
}
