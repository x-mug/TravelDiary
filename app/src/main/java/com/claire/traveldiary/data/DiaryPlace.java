package com.claire.traveldiary.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class DiaryPlace {

    @PrimaryKey
    @NonNull
    private String mPlaceId;
    private int mDiaryId;

    private String mPlaceName;
    private String mCountry;
    private double mLat;
    private double mLng;

    public DiaryPlace() {

        mPlaceId = "";
        mDiaryId = -1;
        mPlaceName = "";
        mCountry = "";
        mLat = -1L;
        mLng = -1L;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public int getDiaryId() {
        return mDiaryId;
    }

    public void setDiaryId(int diaryId) {
        mDiaryId = diaryId;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }


    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }
}
