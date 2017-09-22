package com.tuankhai.travelassistants.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Khai on 05/09/2017.
 */

public class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public ListSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        outRect.left = spacing;
        outRect.right = spacing;

        if (position == 0) { // top edge
            outRect.top = Math.round(spacing * 2);
        } else {
            outRect.top = spacing / 2;
        }
        outRect.bottom = spacing; // item bottom
    }
}
