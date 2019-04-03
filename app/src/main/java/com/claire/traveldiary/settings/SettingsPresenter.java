package com.claire.traveldiary.settings;

import android.annotation.SuppressLint;

import static android.support.v4.util.Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;

    @SuppressLint("RestrictedApi")
    public SettingsPresenter(SettingsContract.View view) {
        mView = checkNotNull(view, "view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
