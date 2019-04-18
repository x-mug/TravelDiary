package com.claire.traveldiary.data.room;

import android.arch.persistence.room.TypeConverter;

import com.claire.traveldiary.data.Diary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DiariesConverter {

    private static Gson gson = new Gson();
    private static Type petListType = new TypeToken<ArrayList<Diary>>() {}.getType();

    @TypeConverter
    public static List<Diary> diariesFromJsonArray(String json) {
        return gson.fromJson(json, petListType);
    }

    @TypeConverter
    public static String diariesToJsonArray(List<Diary> pets) {
        return gson.toJson(pets);
    }
}
