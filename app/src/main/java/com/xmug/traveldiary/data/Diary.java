package com.xmug.traveldiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.xmug.traveldiary.data.room.ImagesConverter;
import com.xmug.traveldiary.data.room.PlacesConverter;
import com.xmug.traveldiary.data.room.TagsConverter;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Diary {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mId;

    private String mTitle;

    private String mDate;

    @TypeConverters(PlacesConverter.class)
    private DiaryPlace mPlace;

    private String mWeather;

    @TypeConverters(ImagesConverter.class)
    private ArrayList<String> mImage;

    private String mContent;

    @TypeConverters(TagsConverter.class)
    private List<String> mTags;


    public Diary() {

        mId = -1;
        mTitle = "";
        mDate = "";
        mPlace = new DiaryPlace();
        mWeather = "ic_sunny";
        mImage = new ArrayList<>();
        mContent = "";
        mTags = null;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }


    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    public DiaryPlace getPlace() {
        return mPlace;
    }

    public void setPlace(DiaryPlace place) {
        mPlace = place;
    }

    public ArrayList<String> getImage() {
        return mImage;
    }

    public void setImage(ArrayList<String> image) {
        mImage = image;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> tags) {
        mTags = tags;
    }

}
