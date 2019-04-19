package com.claire.traveldiary.settings.sync;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class SyncPresenter implements SyncContract.Presenter {

    private SyncContract.View mSyncView;

    public SyncPresenter(@NonNull SyncContract.View syncView) {

        mSyncView = checkNotNull(syncView, "syncView cannot be null!");
        mSyncView.setPresenter(this);
    }


    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {

    }
}
