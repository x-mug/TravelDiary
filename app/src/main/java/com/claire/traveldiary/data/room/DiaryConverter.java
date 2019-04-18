package com.claire.traveldiary.data.room;

import android.arch.persistence.room.TypeConverter;

import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DiaryConverter {
    private static Gson gson = new Gson();
    private static Type diaryListType = new TypeToken<Diary>() {}.getType();

    @TypeConverter
    public static Diary placesFromJsonArray(String json) {
        return gson.fromJson(json, diaryListType);
    }

    @TypeConverter
    public static String placesToJsonArray(Diary diaryPlaces) {
        return gson.toJson(diaryPlaces);
    }
}
