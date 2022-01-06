package com.xmug.traveldiary.data.room;


import androidx.room.TypeConverter;

import com.xmug.traveldiary.data.DiaryPlace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PlacesConverter {

    private static Gson gson = new Gson();
    private static Type placeListType = new TypeToken<DiaryPlace>() {}.getType();

    @TypeConverter
    public static DiaryPlace placesFromJsonArray(String json) {
        return gson.fromJson(json, placeListType);
    }

    @TypeConverter
    public static String placesToJsonArray(DiaryPlace diaryPlaces) {
        return gson.toJson(diaryPlaces);
    }
}
