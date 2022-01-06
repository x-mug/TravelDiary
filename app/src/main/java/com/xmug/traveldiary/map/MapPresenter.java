package com.xmug.traveldiary.map;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.xmug.traveldiary.data.DiaryPlace;
import com.xmug.traveldiary.data.room.DiaryDAO;
import com.xmug.traveldiary.data.room.DiaryDatabase;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;

    private DiaryDatabase mRoomDb;
    private DiaryDAO mDiaryDAO;

    private List<DiaryPlace> mPlaceList;

    @SuppressLint("RestrictedApi")
    public MapPresenter(@NonNull MapContract.View view, @NonNull DiaryDatabase roomDb) {
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
    public void loadDiaryOnMap() {
        mDiaryDAO = mRoomDb.getDiaryDAO();
        mPlaceList = mDiaryDAO.getAllPlaces();
        mView.loadDiaryOnMapUi(mPlaceList);
    }
}
