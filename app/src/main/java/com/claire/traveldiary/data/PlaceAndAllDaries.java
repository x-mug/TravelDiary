package com.claire.traveldiary.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PlaceAndAllDaries {

    @Embedded
    public DiaryPlace mDiaryPlace;

    @Relation(parentColumn = "mId", entityColumn = "mDiaryId")
    public List<Diary> mDiaries;
}
