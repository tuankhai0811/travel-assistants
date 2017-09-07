package com.tuankhai.travelassistants.fragment.controller;

import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.fragment.PlacesFragment;

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
