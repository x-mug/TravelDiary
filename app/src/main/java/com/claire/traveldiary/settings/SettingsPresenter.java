package com.claire.traveldiary.settings;

import android.annotation.SuppressLint;

import static android.support.v4.util.Preconditions.checkNotNull;

public class SettingsPresenter implements SettingsContract.Presenter {

    private static final String TAG = "SettingsPresenter";

    private SettingsContract.View mView;

    @SuppressLint("RestrictedApi")
    public SettingsPresenter(SettingsContract.View view) {
        mView = checkNotNull(view, "view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void openSyncDialog() {
        mView.openSyncDialogUi();
    }

    @Override
    public void openDownloadDialog() {
        mView.openDownloadDialogUi();
    }

    @Override
    public void openFontDialog() {
        mView.openFontDialogUi();
    }

    @Override
    public void loginFacebook() {
        mView.loginFacebookUi();
    }

    @Override
    public void logoutFacebook() {
        mView.logoutFacebookUi();
    }
}
