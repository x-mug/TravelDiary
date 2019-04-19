package com.claire.traveldiary.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.claire.traveldiary.R;

public class SettingsAdapter extends RecyclerView.Adapter{

    private static final String TAG = "SettingsAdapter";

    private SettingsContract.Presenter mPresenter;

    public SettingsAdapter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {

        private TextView mBackup;
        private TextView mTextSize;
        private TextView mLanguage;
        private TextView mPassword;
        private TextView mNotification;
        private TextView mFeedback;

        public SettingsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new SettingsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof SettingsHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
