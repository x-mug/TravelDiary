package com.claire.traveldiary.settings.fontsize;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class FontPresenter implements FontContract.Presenter {

    private FontContract.View mFontsizeView;

    public FontPresenter(@NonNull FontContract.View fontsizeView) {

        mFontsizeView = checkNotNull(fontsizeView, "fontsizeView cannot be null!");
        mFontsizeView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {

    }
}
