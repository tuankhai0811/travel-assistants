package com.tuankhai.travelassistants.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by tuank on 03/09/2017.
 */

public abstract class BaseFragment extends Fragment {
    public View mRootView;
    String TAG = "";

    public abstract boolean onActivityBackPress();
}
