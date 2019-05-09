package com.claire.traveldiary.map;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.claire.traveldiary.MainActivity;
import com.claire.traveldiary.R;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
