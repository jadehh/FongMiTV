package com.fongmi.android.tv.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.databinding.FragmentDownloadFinishBinding;
import com.fongmi.android.tv.ui.base.BaseFragment;

public class DownloadFinishFragment extends BaseFragment {

    private FragmentDownloadFinishBinding mBinding;

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentDownloadFinishBinding.inflate(inflater, container, false);
    }
}
