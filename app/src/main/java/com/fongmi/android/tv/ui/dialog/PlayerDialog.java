package com.fongmi.android.tv.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.databinding.DialogPlayerBinding;
import com.fongmi.android.tv.ui.adapter.PlayerAdapter;
import com.fongmi.android.tv.ui.custom.SpaceItemDecoration;
import com.fongmi.android.tv.utils.ResUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlayerDialog extends BaseDialog implements PlayerAdapter.OnClickListener {

    private final PlayerAdapter adapter;
    private DialogPlayerBinding binding;
    private Listener listener;
    private String title;


    public static PlayerDialog create() {
        return new PlayerDialog();
    }

    public PlayerDialog() {
        this.adapter = new PlayerAdapter(this);
    }

    public PlayerDialog select(int player) {
        this.adapter.setSelected(player);
        return this;
    }

    public PlayerDialog title(String title) {
        this.title = title;
        return this;
    }

    public void show(FragmentActivity activity) {
        for (Fragment f : activity.getSupportFragmentManager().getFragments()) if (f instanceof BottomSheetDialogFragment) ((BottomSheetDialogFragment) f).dismiss();
        show(activity.getSupportFragmentManager(), null);
        this.listener = (Listener) activity;
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return binding = DialogPlayerBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 16));
        binding.recycler.post(() -> binding.recycler.scrollToPosition(adapter.getSelected()));
        binding.recycler.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initEvent() {
        binding.choose.setOnClickListener(this::onShare);
    }

    private void onShare(View view) {
        listener.onPlayerShare(title);
        dismiss();
    }

    @Override
    public void onItemClick(Integer item) {
        listener.onPlayerClick(item);
        dismiss();
    }

    public interface Listener {

        void onPlayerClick(Integer item);

        void onPlayerShare(String title);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ResUtil.dp2px(320), -1);
    }
}
