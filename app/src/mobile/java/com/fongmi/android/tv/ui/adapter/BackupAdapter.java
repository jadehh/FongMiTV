package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.databinding.AdapterBackupBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.github.catvod.utils.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder> {

    private final OnClickListener mListener;
    private List<String> mItems;

    public BackupAdapter(OnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnClickListener {

        void onTextClick(String item);

        void onDeleteClick(String item);
    }

    public BackupAdapter addAll() {
        mItems = new ArrayList<>();
        for(File file : Path.list(Path.tv())) if (file.getAbsolutePath().endsWith(AppDatabase.BACKUP_SUFFIX)) mItems.add(file.getName().replace("." + AppDatabase.BACKUP_SUFFIX, ""));
        Collections.sort(mItems);
        return this;
    }

    public int remove(String item) {
        File file = new File(Path.tv(), item + "." + AppDatabase.BACKUP_SUFFIX);
        if (file.exists()) file.delete();
        mItems.remove(item);
        notifyDataSetChanged();
        return getItemCount();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterBackupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = mItems.get(position);
        holder.binding.text.setText(item);
        holder.binding.text.setOnClickListener(v -> mListener.onTextClick(item));
        holder.binding.delete.setOnClickListener(v -> mListener.onDeleteClick(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterBackupBinding binding;

        ViewHolder(@NonNull AdapterBackupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
