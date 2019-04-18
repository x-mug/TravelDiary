package com.claire.traveldiary.map.showdiary;

import android.support.annotation.NonNull;


import com.claire.traveldiary.data.Diary;

import java.util.List;

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
    public void loadDiaryByPlace(double lat, double lng) {
        mShowDiaryView.openDiaryDialogByPlace(lat, lng);
    }

    @Override
    public void openEdit(Diary diary) {
        mShowDiaryView.openEditUi(diary);
    }

    @Override
    public void closePopup() {
        mShowDiaryView.closePopupUi();
    }

    @Override
    public void start() {

    }
}
