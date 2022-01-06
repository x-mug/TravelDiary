package com.xmug.traveldiary.component;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public GridSpacingItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace/2;
        outRect.right = mSpace/2;
        outRect.bottom = mSpace;

        //Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1)
            outRect.top = mSpace;
    }
}
