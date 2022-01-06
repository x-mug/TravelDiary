package com.xmug.traveldiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private String mName;
    private String mPassword;
    private String mEmail;
    private String mPicture;

    public User() {
        mName = "";
        mPassword = "123";
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

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
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
