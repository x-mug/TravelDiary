package com.claire.traveldiary.map.showdiary;

import android.support.annotation.NonNull;


import com.claire.traveldiary.data.Diary;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class ShowDiaryPresenter implements ShowDiaryContract.Presenter {

    private ShowDiaryContract.View mShowDiaryView;

    public ShowDiaryPresenter(@NonNull ShowDiaryContract.View showDiaryViewiew) {

        mShowDiaryView = checkNotNull(showDiaryViewiew, "showdiaryview cannot be null!");
        mShowDiaryView.setPresenter(this);
    }


    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadDiaryByPlace(Diary diary) {
        mShowDiaryView.openDiaryDialogByPlace(diary);
    }

    @Override
    public void start() {

    }
}