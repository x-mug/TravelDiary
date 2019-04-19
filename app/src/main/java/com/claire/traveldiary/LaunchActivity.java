package com.claire.traveldiary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.claire.traveldiary.base.BaseActivity;

public class LaunchActivity extends BaseActivity {

    private int mTotalDuration = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Handler().postDelayed(() -> {

            new Handler().postDelayed(() -> {

                finish();

            }, mTotalDuration / 3 * 2);

        }, mTotalDuration);

    }

}
