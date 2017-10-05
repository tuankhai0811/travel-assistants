package com.tuankhai.travelassistants.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tuank on 05/10/2017.
 */

public class PlaceQueryDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public PlaceQueryDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        outRect.left = 0;
        outRect.right = 0;

        if (position == 0) { // top edge
            outRect.top = spacing;
        } else {
            outRect.top = 0;
        }
        outRect.bottom = spacing; // item bottom
    }
}