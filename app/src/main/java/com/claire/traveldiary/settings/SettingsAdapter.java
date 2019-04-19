package com.claire.traveldiary.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claire.traveldiary.R;

public class SettingsAdapter extends RecyclerView.Adapter{

    private static final String TAG = "SettingsAdapter";

    private SettingsContract.Presenter mPresenter;

    public SettingsAdapter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {

        private ImageView mUserImg;
        private TextView mUserName;
        private TextView mSync;
        private TextView mTextSize;
        private TextView mLanguage;
        private TextView mPassword;
        private TextView mNotification;
        private TextView mFeedback;

        public SettingsHolder(@NonNull View itemView) {
            super(itemView);

            mUserImg = itemView.findViewById(R.id.img_profile);
            mUserName = itemView.findViewById(R.id.tv_user_name);
            mSync = itemView.findViewById(R.id.tv_sync);
            mTextSize = itemView.findViewById(R.id.tv_textsize);
            mLanguage = itemView.findViewById(R.id.tv_language);
            mPassword = itemView.findViewById(R.id.tv_key);
            mNotification = itemView.findViewById(R.id.tv_notification);
            mFeedback = itemView.findViewById(R.id.tv_mail);
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

            ((SettingsHolder) holder).mSync.setOnClickListener(v -> {
                mPresenter.openSyncDialog();
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
