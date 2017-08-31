package com.tuankhai.travelassistants.fragment;

import com.tuankhai.travelassistants.activity.BaseActivity;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesController {
    private PlacesFragment placesFragment;
    private BaseActivity mActivity;

    public PlacesController(PlacesFragment fragment) {
        placesFragment = fragment;
        mActivity = (BaseActivity) fragment.getActivity();
    }
}
