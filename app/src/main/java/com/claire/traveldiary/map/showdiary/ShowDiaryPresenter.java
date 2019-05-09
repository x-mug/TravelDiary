package com.claire.traveldiary.map.showdiary;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;


import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class ShowDiaryPresenter implements ShowDiaryContract.Presenter {

    private static final String TAG = "ShowDiaryPresenter";

    private ShowDiaryContract.View mView;

    private DiaryDatabase mRoomDb;

    private List<DiaryPlace> mPlaceList;

    public ShowDiaryPresenter(@NonNull ShowDiaryContract.View showDiaryViewiew, @NonNull DiaryDatabase roomDb) {
        mView = checkNotNull(showDiaryViewiew, "showdiaryview cannot be null!");
        mRoomDb = checkNotNull(roomDb, "roomDb cannot be null!");
        mView.setPresenter(this);
    }


    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadDiaryByPlace(double lat, double lng) {
        mPlaceList = mRoomDb.getDiaryDAO().getPlacebyLatlng(lat, lng);
        List<Diary> diaryList = new ArrayList<>();

        for (int i = 0; i < mPlaceList.size(); i++) {
            Diary diary = new Diary();
            diary  = mRoomDb.getDiaryDAO().getDiarybyId(mPlaceList.get(i).getDiaryId());
            diaryList.add(diary);

            Log.d(TAG, "diary size: " + diaryList.size());
        }
        mView.loadDiaryByPlaceUi(diaryList);
        Log.d(TAG,"how many diaries in that place " + mPlaceList.size());
        Log.d(TAG, "diary place: " + mPlaceList.get(0).getPlaceName());
    }

    @Override
    public void openEdit(Diary diary) {
        mView.openEditUi(diary);
    }

    @Override
    public void closePopup() {
        mView.closePopupUi();
    }

    @Override
    public void setFontType(TextView title, TextView date) {
        mView.setFontTypeUi(title, date);
    }

    @Override
    public void start() {}
}
