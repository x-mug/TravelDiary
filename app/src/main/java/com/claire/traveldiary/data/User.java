package com.claire.traveldiary.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private long mId;
    private String mName;
    private String mEmail;
    private String mPicture;

    public User() {
        mId = -1;
        mName = "";
        mEmail = "";
        mPicture = "";
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }
}
