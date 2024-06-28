package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.AdapterDownloadBinding;
import com.fongmi.android.tv.utils.FileUtil;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {


    public final List<DownloadTask> list;
    private final DownloadAdapter.OnClickListener mListener;

    public DownloadAdapter(OnClickListener mListener, List<DownloadTask> list) {
        this.mListener = mListener;
        this.list = list;
    }

    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterDownloadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder holder, int position) {
        DownloadTask task = list.get(position);
        holder.binding.fileName.setText(task.getFileName());
        holder.binding.downloadSize.setText(String.format(ResUtil.getString(R.string.download_size), FileUtil.byteCountToDisplaySize(task.getFileSize()), FileUtil.byteCountToDisplaySize(task.getDownloadSize())));
        holder.binding.downloadSpeed.setText((String.format(ResUtil.getString(R.string.download_speed), FileUtil.byteCountToDisplaySize(task.getDownloadSpeed()))));
        setViewVisibility(holder,View.VISIBLE);
        if (task.getDownloadSize() != 0 && task.getFileSize() != 0) {
            holder.binding.numberProgressBar.setProgress((int) (BigDecimal.valueOf((float) task.getDownloadSize() / task.getFileSize()).setScale(2, RoundingMode.HALF_UP).doubleValue() * 100));
        }
        int status = task.getTaskStatus();
        if ((status == Constant.DOWNLOAD_STOP) || (status == Constant.DOWNLOAD_CONNECTION && task.getTaskId() == 0)) {
            holder.binding.downloadSpeed.setText((String.format(ResUtil.getString(R.string.download_speed), "0KB")));
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_play));
        } else if (status == Constant.DOWNLOAD_CONNECTION) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_connect));
        } else if (status == Constant.DOWNLOAD_FAIL) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_fail));
        } else if (status == Constant.DOWNLOAD_WAIT) {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_wait));
        } else if (status == Constant.DOWNLOAD_SUCCESS){
            holder.binding.numberProgressBar.setProgress(0);
            holder.binding.downloadSize.setText(FileUtil.byteCountToDisplaySize(task.getFileSize()));
            setViewVisibility(holder,View.GONE);
        } else {
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_pause));
        }
        holder.binding.startTask.setOnClickListener(v -> startTask(task));
        holder.binding.deleteTask.setOnClickListener(v -> deleteTask(task, holder));
        if (task.getFile()) {
            holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_folder));
        } else {
          setIcon(task,holder);
        }
        holder.binding.downloadIcon.setOnClickListener(v -> downloadIcon(task));
        holder.binding.getRoot().setOnClickListener(v -> downloadIcon(task));
    }

    private void setViewVisibility(ViewHolder holder,int visible){
        holder.binding.downloadSpeed.setVisibility(visible);
        holder.binding.startTask.setVisibility(visible);
        holder.binding.numberProgressBar.setVisibility(visible);
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

    private void setIcon(DownloadTask task,ViewHolder holder){
        switch (Util.getFileType(task.getFileName())) {
            case 1:
                holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_video));
                break;
            case 2:
                holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_exe));
                break;
            case 3:
                holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_zip));
            break;
            default:
                holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_file));
                break;
        }
    }

    private void deleteTask(DownloadTask task, @NonNull DownloadAdapter.ViewHolder holder) {
        holder.binding.numberProgressBar.setProgress(0);
        mListener.deleteTask(task);
    }

    private void downloadIcon(DownloadTask task) {
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

        void deleteTask(DownloadTask task);

        void refreshData(List<DownloadTask> tasks);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDownloadBinding binding;

        ViewHolder(@NonNull AdapterDownloadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
