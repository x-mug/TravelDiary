package com.xmug.traveldiary.data;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Diary.class, parentColumns = "mId", childColumns = "mDiaryId", onDelete = CASCADE))
public class DiaryPlace {

    @PrimaryKey
    @NonNull
    private int mDiaryId;

    private String mPlaceId;
    private String mPlaceName;
    private String mCountry;
    private double mLat;
    private double mLng;

    public DiaryPlace() {

        mPlaceId = "";
        mDiaryId = -1;
        mPlaceName = "";
        mCountry = "";
        mLat = 0.0;
        mLng = 0.0;
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
