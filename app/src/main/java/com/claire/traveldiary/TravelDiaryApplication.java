package com.claire.traveldiary;

import android.app.Application;
import android.content.Context;

public class TravelDiaryApplication extends Application {

    private static Context mContext;
    //private static StylishSQLiteHelper mStylishSQLiteHelper;

    public TravelDiaryApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //mStylishSQLiteHelper = null;
    }

    public static Context getAppContext() {
        return mContext;
    }

//    public static StylishSQLiteHelper getSQLiteHelper() {
//        if (mStylishSQLiteHelper == null) mStylishSQLiteHelper = new StylishSQLiteHelper(getAppContext());
//        return mStylishSQLiteHelper;
//    }
}
