package com.claire.traveldiary.mainpage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claire.traveldiary.R;

public class MainPageAdapter extends RecyclerView.Adapter {

    private MainPageContract.Presenter mPresenter;

    public MainPageAdapter(MainPageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private class MainPageViewHolder extends RecyclerView.ViewHolder {

        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MainPageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
