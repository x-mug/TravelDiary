package com.claire.traveldiary;

import android.app.Application;
import android.content.Context;

public class TravelDiaryApplication extends Application {

    private static Context mContext;

    public TravelDiaryApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

}
