package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.AdapterDownloadFinishBinding;
import com.fongmi.android.tv.utils.FileUtil;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.Util;


import java.util.List;

public class DownloadFinishAdapter extends RecyclerView.Adapter<DownloadFinishAdapter.ViewHolder> {


    private final DownloadFinishAdapter.OnClickListener mListener;
    public final List<DownloadTask> list;


    public DownloadFinishAdapter(OnClickListener mListener, List<DownloadTask> list) {
        this.mListener = mListener;
        this.list = list;
    }

    @NonNull
    @Override
    public DownloadFinishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterDownloadFinishBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadFinishAdapter.ViewHolder holder, int position) {
        DownloadTask task = list.get(position);
        if (task.getFile()){
            holder.binding.openFile.setVisibility(View.VISIBLE);
            holder.binding.openFile.setOnClickListener(v->openFile(task));
            holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_folder));
        }else{
            switch (Util.getFileType(task.getFileName())){
                case 1:
                    holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_video));
                    break;
                case 2:
                    holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_exe));
                    break;
                default:
                    holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_file));
                    break;
            }
        }
        holder.binding.fileName.setText(task.getFileName());
        holder.binding.deleteTask.setOnClickListener(v-> deleTask(task));
        holder.binding.downloadSize.setText(FileUtil.byteCountToDisplaySize(task.getFileSize()));
        holder.binding.downloadIcon.setOnClickListener(v -> downloadIcon(task));
        holder.binding.getRoot().setOnClickListener(v -> downloadIcon(task));
    }


    private void downloadIcon(DownloadTask task){
        mListener.openFile(task);
    }

    private void deleTask(DownloadTask task){
        mListener.deleTask(task);
    }

    private void openFile(DownloadTask task){
        mListener.openFile(task);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClickListener {
        void openFile(DownloadTask task);
        void deleTask(DownloadTask task);
        void refreshData(List<DownloadTask> tasks);

    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDownloadFinishBinding binding;
        ViewHolder(@NonNull AdapterDownloadFinishBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
