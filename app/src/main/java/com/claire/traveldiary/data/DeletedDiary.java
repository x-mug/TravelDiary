package com.claire.traveldiary.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DeletedDiary {

    @PrimaryKey
    @NonNull
    private int mId;

    public DeletedDiary() {
        mId = -1;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
