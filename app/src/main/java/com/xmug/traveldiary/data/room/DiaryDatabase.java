package com.xmug.traveldiary.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.xmug.traveldiary.data.DeletedDiary;
import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.DiaryPlace;
import com.xmug.traveldiary.data.User;

@Database(entities = {Diary.class, DiaryPlace.class, User.class, DeletedDiary.class}, version = 1, exportSchema = false)
public abstract class DiaryDatabase extends RoomDatabase {

    private static DiaryDatabase INSTANCE;

    public abstract DiaryDAO getDiaryDAO();

    public static DiaryDatabase getIstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DiaryDatabase.class, "diary_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
