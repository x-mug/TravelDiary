package com.xmug.traveldiary;

import android.os.Handler;
import android.os.Bundle;

import com.xmug.traveldiary.base.BaseActivity;


public class LaunchActivity extends BaseActivity {

    private int mTotalDuration = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Handler().postDelayed(() -> {

            new Handler().postDelayed(this::finish, mTotalDuration / 3 * 2);

        }, mTotalDuration);
    }
}
