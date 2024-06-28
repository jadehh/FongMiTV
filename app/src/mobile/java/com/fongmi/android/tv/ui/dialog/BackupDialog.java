package com.fongmi.android.tv.ui.dialog;

import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.fongmi.android.tv.databinding.DialogHistoryBinding;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.impl.BackupCallback;
import com.fongmi.android.tv.ui.adapter.BackupAdapter;
import com.fongmi.android.tv.ui.custom.SpaceItemDecoration;
import com.github.catvod.utils.Path;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

public class BackupDialog implements BackupAdapter.OnClickListener {

    private final DialogHistoryBinding binding;
    private final BackupCallback callback;
    private final BackupAdapter adapter;
    private final AlertDialog dialog;

    public static BackupDialog create(Fragment fragment) {
        return new BackupDialog(fragment);
    }


    public BackupDialog(Fragment fragment) {
        this.callback = (BackupCallback) fragment;
        this.binding = DialogHistoryBinding.inflate(LayoutInflater.from(fragment.getContext()));
        this.dialog = new MaterialAlertDialogBuilder(fragment.getActivity()).setView(binding.getRoot()).create();
        this.adapter = new BackupAdapter(this);
    }

    public void show() {
        setRecyclerView();
        setDialog();
    }

    private void setRecyclerView() {
        binding.recycler.setHasFixedSize(true);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 8));
        binding.recycler.setAdapter(adapter.addAll());
    }

    private void setDialog() {
        if (adapter.getItemCount() == 0) return;
        dialog.getWindow().setDimAmount(0);
        dialog.show();
    }

    @Override
    public void onTextClick(String item) {
        callback.restore(new File(Path.tv(), item + "." + AppDatabase.BACKUP_SUFFIX));
        dialog.dismiss();
    }

    @Override
    public void onDeleteClick(String item) {
        if (adapter.remove(item) == 0) dialog.dismiss();
    }
}
