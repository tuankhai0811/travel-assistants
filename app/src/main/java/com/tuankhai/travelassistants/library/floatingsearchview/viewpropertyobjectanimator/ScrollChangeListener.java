package com.tuankhai.travelassistants.library.floatingsearchview.viewpropertyobjectanimator;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by tuank on 03/09/2017.
 */

class ScrollChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener {

    private IntValues mScrollX;
    private IntValues mScrollY;

    ScrollChangeListener(View view) {
        super(view);
    }

    private int currentScrollX() {
        return hasView() ? mView.get().getScrollX() : 0;
    }

    private int currentScrollY() {
        return hasView() ? mView.get().getScrollY() : 0;
    }

    public void scrollX(int scrollX) {
        mScrollX = new IntValues(currentScrollX(), scrollX);
    }

    public void scrollXBy(int scrollXBy) {
        mScrollX = new IntValues(currentScrollX(), currentScrollX() + scrollXBy);
    }

    public void scrollY(int scrollY) {
        mScrollY = new IntValues(currentScrollY(), scrollY);
    }

    public void scrollYBy(int scrollYBy) {
        mScrollY = new IntValues(currentScrollY(), currentScrollY() + scrollYBy);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (hasView()) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            int scrollX = currentScrollX();
            int scrollY = currentScrollY();
            if (mScrollX != null) {
                scrollX = (int) calculateAnimatedValue(mScrollX.mFrom, mScrollX.mTo, animatedFraction);
            }
            if (mScrollY != null) {
                scrollY = (int) calculateAnimatedValue(mScrollY.mFrom, mScrollY.mTo, animatedFraction);
            }
            mView.get().scrollTo(scrollX, scrollY);
        }
    }

}
