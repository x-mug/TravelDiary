package com.claire.traveldiary.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.claire.traveldiary.R;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
    }

    public void setTypeface(int style) {
        switch (style) {
            case 0:
                Typeface typeface = getResources().getFont(R.font.allura_regular);
                break;
            case 1:

        }

    }
}
