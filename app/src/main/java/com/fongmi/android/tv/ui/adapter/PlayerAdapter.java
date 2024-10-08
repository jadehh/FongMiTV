package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.databinding.AdapterPlayerBinding;
import com.fongmi.android.tv.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final OnClickListener mListener;
    private final List<Integer> mItems;
    private final String[] mPlayers;
    private int selected;

    public PlayerAdapter(OnClickListener listener) {
        this.mListener = listener;
        this.mPlayers = ResUtil.getStringArray(R.array.select_player);
        this.mItems = new ArrayList<>();
        for(int i= 0; i<this.mPlayers.length; i++) this.mItems.add(i);
    }

    public interface OnClickListener {

        void onItemClick(Integer item);
    }

    public void setSelected(int player) {
        this.selected = player;
    }

    public int getSelected() {
        return this.selected;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterPlayerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer item = mItems.get(position);
        holder.binding.text.setText(mPlayers[item]);
        holder.binding.text.setActivated(item == selected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final AdapterPlayerBinding binding;

        public ViewHolder(@NonNull AdapterPlayerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Integer item = mItems.get(getLayoutPosition());
            mListener.onItemClick(item);
        }
    }
}
