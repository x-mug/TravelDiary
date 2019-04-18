package com.claire.traveldiary.component;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.claire.traveldiary.util.Util;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {


    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the no first item to avoid double space between items
        if (parent.getChildPosition(view) != 0) {
            outRect.left = space;
            outRect.right = 0;
            outRect.bottom = 0;
            outRect.top = 0;
        }
    }


}
