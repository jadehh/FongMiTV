package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.AdapterDownloadingBinding;
import com.fongmi.android.tv.ui.activity.DownloadManageActivity;
import com.fongmi.android.tv.utils.FileUtil;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.TimeUtil;
import com.fongmi.quickjs.bean.Res;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.ViewHolder> {


    private final DownloadingAdapter.OnClickListener mListener;
    private final List<DownloadTask> list;


    public DownloadingAdapter(OnClickListener mListener, List<DownloadTask> list) {
        this.mListener = mListener;
        this.list = list;
    }

    @NonNull
    @Override
    public DownloadingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterDownloadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadingAdapter.ViewHolder holder, int position) {
        DownloadTask task = list.get(position);
        holder.binding.fileName.setText(task.getFileName());
        holder.binding.downloadSize.setText(String.format(ResUtil.getString(R.string.download_size), FileUtil.byteCountToDisplaySize(task.getFileSize()), FileUtil.byteCountToDisplaySize(task.getDownloadSize())));
        holder.binding.downloadSpeed.setText((String.format(ResUtil.getString(R.string.download_speed), FileUtil.byteCountToDisplaySize(task.getDownloadSpeed()))));
        if (task.getDownloadSize() != 0 && task.getFileSize() != 0) {
            holder.binding.numberProgressBar.setProgress((int) (BigDecimal.valueOf((float) task.getDownloadSize() / task.getFileSize()).setScale(2, RoundingMode.HALF_UP).doubleValue() * 100));
        }
        int status = task.getTaskStatus();
        if ((status == Constant.DOWNLOAD_STOP) || (status == Constant.DOWNLOAD_CONNECTION && task.getTaskId() == 0)) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_download));
        } else if (status == Constant.DOWNLOAD_CONNECTION) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_connent));
        } else if (status == Constant.DOWNLOAD_FAIL) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_fail));
        } else if (status == Constant.DOWNLOAD_WAIT) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_wait));
        } else {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_stop));
        }
        holder.binding.startTask.setOnClickListener(v -> startTask(task));
        holder.binding.deleteTask.setOnClickListener(v -> deleteTask(task));
        if (task.getFile()){
            holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_floder));
        }else{
            holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_video));
        }
        holder.binding.downloadIcon.setOnClickListener(v -> downloadIcon(task));
    }
    private void startTask(DownloadTask task) {
        if (task.getTaskStatus() == Constant.DOWNLOAD_FAIL) {
            mListener.stopTask(task);
            mListener.startTask(task);
        } else if ((task.getTaskStatus() == Constant.DOWNLOAD_STOP) ||
                (task.getTaskStatus() == Constant.DOWNLOAD_CONNECTION && task.getTaskId() == 0) ||
                task.getTaskStatus() == Constant.DOWNLOAD_WAIT) {
            mListener.startTask(task);
        } else {
            mListener.stopTask(task);
        }
    }
    private void deleteTask(DownloadTask task) {
        mListener.deleTask(task);
    }

    private void downloadIcon(DownloadTask task){
        mListener.openFile(task);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClickListener {
        void startTask(DownloadTask task);
        void stopTask(DownloadTask task);
        void openFile(DownloadTask task);
        void deleTask(DownloadTask task);
        void refreshData(List<DownloadTask> tasks);
        void alert(String msg, int msgType);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDownloadingBinding binding;
        ViewHolder(@NonNull AdapterDownloadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
