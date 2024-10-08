package com.fongmi.android.tv.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Constant;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.DownloadMsg;
import com.fongmi.android.tv.bean.DownloadTask;
import com.fongmi.android.tv.databinding.AdapterDownloadBinding;
import com.fongmi.android.tv.utils.FileUtil;
import com.fongmi.android.tv.utils.ImgUtil;
import com.fongmi.android.tv.utils.ResUtil;

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
        setRefreshView(task,holder);
        initEvent(task,holder);
        if (task.getTaskStatus() != Constant.DOWNLOAD_SUCCESS) setDownloadingRefreshView(task,holder);
        else setDownloadFinishView(task,holder);
    }


    private void setViewVisibility(ViewHolder holder,int visible){
        holder.binding.downloadSpeed.setVisibility(visible);
        holder.binding.startTask.setVisibility(visible);
        holder.binding.numberProgressBar.setVisibility(visible);
    }



    private void setDownloadingNoRefreshView(DownloadTask task,ViewHolder holder){
        setViewVisibility(holder,View.VISIBLE);
        holder.binding.openFile.setVisibility(View.GONE);
    }

    private void setDownloadFinishView(DownloadTask task,ViewHolder holder){
        setViewVisibility(holder,View.GONE);
        holder.binding.downloadSize.setText( FileUtil.byteCountToDisplaySize(task.getFileSize()));
        if (task.getFile()){
            holder.binding.openFile.setVisibility(View.VISIBLE);
        }
    }

    private  void setDownloadingRefreshView(DownloadTask task,ViewHolder holder){
        setDownloadingNoRefreshView(task,holder);
        setViewVisibility(holder,View.VISIBLE);
        if (task.getFileSize() == 0) holder.binding.downloadSize.setText(FileUtil.byteCountToDisplaySize(task.getDownloadSize()));
        else holder.binding.downloadSize.setText(String.format(ResUtil.getString(R.string.download_size), FileUtil.byteCountToDisplaySize(task.getFileSize()), FileUtil.byteCountToDisplaySize(task.getDownloadSize())));
        if (task.getPercent() != 0)  holder.binding.numberProgressBar.setProgress(task.getPercent());
        if (task.getDownloadSize() != 0 && task.getFileSize() != 0) holder.binding.numberProgressBar.setProgress((int) (BigDecimal.valueOf((float) task.getDownloadSize() / task.getFileSize()).setScale(2, RoundingMode.HALF_UP).doubleValue() * 100));
        int status = task.getTaskStatus();
        if (status != Constant.DOWNLOAD_LOADING) holder.binding.downloadSpeed.setText((String.format(ResUtil.getString(R.string.download_speed), "0 KB")));
        else holder.binding.downloadSpeed.setText((String.format(ResUtil.getString(R.string.download_speed), FileUtil.byteCountToDisplaySize(task.getDownloadSpeed()))));
        if (status == Constant.DOWNLOAD_STOP)
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_play));
        else if (status == Constant.DOWNLOAD_CONNECTION)
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_connect));
        else if (status == Constant.DOWNLOAD_FAIL)
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_fail));
        else if (status == Constant.DOWNLOAD_WAIT)
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_wait));
        else if (status == Constant.DOWNLOAD_LOADING)
            holder.binding.startTask.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_pause));
    }



    private void initEvent(DownloadTask task,ViewHolder holder){
        holder.binding.startTask.setOnClickListener(v -> startTask(task));
        holder.binding.deleteTask.setOnClickListener(v -> deleteTask(task, holder));
        holder.binding.downloadIcon.setOnClickListener(v -> downloadIcon(task));
        holder.binding.getRoot().setOnClickListener(v -> downloadIcon(task));
    }




    private void startTask(DownloadTask task) {
      if (task.getTaskStatus() == Constant.DOWNLOAD_STOP)  mListener.startTask(task);
      else if (task.getTaskStatus() == Constant.DOWNLOAD_LOADING) {
          mListener.stopTask(task);
      }
    }


    private void setRefreshView(DownloadTask task,ViewHolder holder){
        setDownloadIcon(task,holder);
        holder.binding.fileName.setText(task.getFileName());
    }


    private void setDownloadIcon(DownloadTask task,ViewHolder holder){
        if (task.getThumbnailPath() != null && task.getThumbnailPath().length() > 0)
            ImgUtil.loadVod(task.getFileName(), task.getThumbnailPath(),holder.binding.downloadIcon);
        else{
            if (task.getFile())  holder.binding.downloadIcon.setImageDrawable(ResUtil.getDrawable(R.drawable.ic_download_folder));
            else  setIcon(task,holder);
        }
    }

    private void setIcon(DownloadTask task,ViewHolder holder){
        switch (FileUtil.getFileType(task.getFileName())) {
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