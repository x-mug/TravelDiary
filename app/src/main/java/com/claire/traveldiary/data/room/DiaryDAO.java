package com.claire.traveldiary.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.claire.traveldiary.data.DeletedDiary;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.User;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DiaryDAO {

    //Diary
    @Insert
    void insertDiary(Diary diary);

    @Update(onConflict = REPLACE)
    void updateDaries(Diary diaries);

    @TypeConverters(ImagesConverter.class)
    @Query("UPDATE Diary SET mImage = :image WHERE mId = :id" )
    void updateImageFromFirebase(ArrayList<String> image, int id);


    @Query("SELECT * FROM diary")
    Diary getDiary();

    @Query("SELECT * FROM diary ORDER BY mDate ASC")
    List<Diary> getAllDiaries();

    @Query("DELETE FROM Diary WHERE mId = :id")
    void deleteDiarybyId(int id);

    @Query("SELECT * FROM diary WHERE mId = :id")
    Diary getDiarybyId(int id);

    @Query("DELETE FROM Diary")
    void deleteAllDiaries();


    //DeletedDiary
    @Insert
    void insertDeletedDiary(DeletedDiary deletedDiary);

    @Query("DELETE FROM DeletedDiary WHERE mId = :id")
    void removeDeletedDiary(int id);


    @Query("SELECT * FROM deleteddiary")
    List<DeletedDiary> getAllDeletedDiariesId();

    @Query("DELETE FROM DeletedDiary")
    void removeAllDeletedDiaries();

    //User
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user")
    User getUser();

    @Query("SELECT * FROM user WHERE mId = :id")
    User getUserbyId(long id);

    @Update(onConflict = REPLACE)
    void updateUser(User user);

    @Query("DELETE FROM user")
    void cleanUser();

    //Place
    @Insert
    void insertPlace(DiaryPlace places);

    @Update(onConflict = REPLACE)
    void updatePlaces(DiaryPlace places);

    @Query("SELECT * FROM DiaryPlace")
    List<DiaryPlace> getAllPlaces();

    @Query("SELECT * FROM diaryplace WHERE mDiaryId = :id")
    DiaryPlace getPlacebyDiaryId(int id);

    @Query("DELETE FROM DiaryPlace WHERE mDiaryId = :id")
    void deletePlacebyId(int id);

    //get diary by place by placeName
    @Query("SELECT * FROM diaryplace WHERE " + " mLat = :lat AND mLng = :lng")
    List<DiaryPlace> getPlacebyLatlng(double lat, double lng);

    //Search
    @Query("SELECT * FROM Diary WHERE mTitle LIKE :title OR " + "mTags LIKE :tags")
    List<Diary> getDiariesBySearch(String title, String tags);

    @Query("DELETE FROM DiaryPlace")
    void deleteAllPlaces();



    default void insertOrUpdateDiary(Diary diary) {
        Diary diaryFromDB = getDiarybyId(diary.getId());

        if (diaryFromDB != null)
            updateDaries(diary);
        else
            insertDiary(diary);
    }

    default void insertOrUpdatePlace(DiaryPlace place) {
        DiaryPlace placeFromDB = getPlacebyDiaryId(place.getDiaryId());

        if (placeFromDB != null)
            updatePlaces(place);
        else
            insertPlace(place);
    }

    default void insertOrUpdateUser(User user) {
        User userFromDB = getUserbyId(user.getId());

        if (userFromDB != null)
            updateUser(user);
        else
            insertUser(user);
    }

}
