package com.fongmi.android.tv.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.AdapterDeviceBinding;

import java.util.List;

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.ViewHolder> {

    private final DownloadingAdapter.OnClickListener mListener;

    private List<DownloadTask> list;


    public DownloadingAdapter(OnClickListener mListener,List<DownloadTask> list) {
        this.mListener = mListener;
        this.list = list;
    }


    @NonNull
    @Override
    public DownloadingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadingAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnClickListener {

        void startTask(DownloadTask task);
        void sopTask(DownloadTask task);
        void openFile(DownloadTask task);
        void deleTask(DownloadTask task);
        void refreshData( List<DownloadTask> tasks);
        void alert(String msg,int msgType);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterDeviceBinding binding;

        ViewHolder(@NonNull AdapterDeviceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
