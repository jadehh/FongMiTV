package com.fongmi.android.tv.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.databinding.DialogMenuBinding;
import com.fongmi.android.tv.impl.MenuKeyCallback;
import com.fongmi.android.tv.ui.adapter.MenuAdapter;
import com.fongmi.android.tv.ui.custom.SpaceItemDecoration;
import com.fongmi.android.tv.utils.ResUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuKeyDialog implements MenuAdapter.OnClickListener {
    private final DialogMenuBinding binding;
    private final MenuKeyCallback callback;
    private final MenuAdapter adapter;
    private final AlertDialog dialog;

    private final Activity activity;


    public static MenuKeyDialog create(Activity activity) {
        return new MenuKeyDialog(activity);
    }

    public MenuKeyDialog(Activity activity) {
        String[] items = ResUtil.getStringArray(R.array.select_home_menu_key);
        List<String> mItems = new ArrayList<>(Arrays.asList(items));
        this.callback = (MenuKeyCallback) activity;
        this.adapter = new MenuAdapter(this, mItems);
        this.activity = activity;
        this.binding = DialogMenuBinding.inflate(LayoutInflater.from(activity));
        this.dialog = new MaterialAlertDialogBuilder(activity).setView(binding.getRoot()).create();
    }

    public void show() {
        initView();
    }

    private int getCount() {
        return 3;
    }

    private float getWidth() {
        return 0.4f + (getCount() - 1) * 0.2f;
    }

    private void initView() {
        setRecyclerView();
        setDialog();
    }

    private void setRecyclerView() {
        binding.recycler.setAdapter(adapter);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setItemAnimator(null);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(getCount(), 16));
        binding.recycler.setLayoutManager(new GridLayoutManager(dialog.getContext(), getCount()));
        binding.recycler.post(() -> binding.recycler.scrollToPosition(Setting.getHomeMenuKey()));

    }

    private void setDialog() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (ResUtil.getScreenWidth() * getWidth());
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setDimAmount(0);
        dialog.show();
    }

    @Override
    public void onItemClick(int position) {
        if (dialog != null) dialog.dismiss();
        callback.onMenuKeyItemClick(position);
    }

}
