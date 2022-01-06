package com.xmug.traveldiary.settings.language;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class LanguagePresenter implements LanguageContract.Presenter {

    private LanguageContract.View mLanguageView;

    public LanguagePresenter(@NonNull LanguageContract.View languageView) {

        mLanguageView = checkNotNull(languageView, "languageView cannot be null!");
        mLanguageView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void start() {

    }
}
