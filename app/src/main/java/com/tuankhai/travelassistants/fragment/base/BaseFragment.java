package com.tuankhai.travelassistants.fragment.base;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by tuank on 03/09/2017.
 */

public abstract class BaseFragment extends Fragment {
    public View mRootView;
    public String TAG = "";

    public abstract boolean onActivityBackPress();
}
