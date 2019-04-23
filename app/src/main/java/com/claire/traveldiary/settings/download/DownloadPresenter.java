package com.claire.traveldiary.settings.download;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mDownloadView;

    public DownloadPresenter(@NonNull DownloadContract.View downloadView) {

        mDownloadView = checkNotNull(downloadView, "downloadView cannot be null!");
        mDownloadView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {

    }
}
