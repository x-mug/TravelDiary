package com.claire.traveldiary.edit;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;


import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;

import static android.support.v4.util.Preconditions.checkNotNull;

public class EditPresenter implements EditContract.Presenter {

    private static final String TAG = "EditPresenter";

    private EditContract.View mView;

    private DiaryDatabase mRoomDb;
    private DiaryDAO mDiaryDAO;

    @SuppressLint("RestrictedApi")
    public EditPresenter(@NonNull EditContract.View view, @NonNull DiaryDatabase roomDb) {
        mView = checkNotNull(view, "view cannot be null!");
        mRoomDb = checkNotNull(roomDb, "roomDb cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void openWeatherDialog() {
        mView.openWeatherDialogUi();
    }

    @Override
    public void openGallery() {
        mView.openGalleryUi();
    }

    @Override
    public void openDatePicker() {
        mView.openDatePickerUi();
    }


    @Override
    public void clickSaveDiary() {
        Log.d(TAG,"Successful save diary to room!");
        mView.clickSaveDiaryUi();
    }

    @Override
    public void insertOrUpdateDiary(Diary diary) {
        mDiaryDAO = mRoomDb.getDiaryDAO();
        mDiaryDAO.insertOrUpdateDiary(diary);
        Log.d(TAG, "Diary size" + mDiaryDAO.getAllDiaries().size());
    }

    @Override
    public void insertOrUpdatePlace(DiaryPlace diaryPlace) {
        mDiaryDAO = mRoomDb.getDiaryDAO();
        mDiaryDAO.insertOrUpdatePlace(diaryPlace);
        Log.d(TAG, "Place size" + mDiaryDAO.getAllPlaces().size());
    }

    @Override
    public void loadDiaryData(Diary diary) {
        mView.openEditPageUi(diary);
    }

    @Override
    public void clickEditDiary() {
        mView.clickEditDiaryUi();
    }

}
