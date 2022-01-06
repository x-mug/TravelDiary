package com.xmug.traveldiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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
