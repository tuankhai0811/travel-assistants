package com.tuankhai.travelassistants.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Khai on 11/09/2017.
 */

public class SpacingItemDecorationHorizontal extends RecyclerView.ItemDecoration {

    private int spacing;

    public SpacingItemDecorationHorizontal(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (position == 0) {
            outRect.left = spacing;
        } else {
            outRect.left = 0;
        }
        outRect.right = spacing;
        outRect.top = 0;
        outRect.bottom = 0;
    }
}
