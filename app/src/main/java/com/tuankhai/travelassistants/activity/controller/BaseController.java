package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.fragment.BaseFragment;
import com.tuankhai.travelassistants.fragment.PlacesFragment;
import com.tuankhai.travelassistants.fragment.SearchPlaceFragment;
import com.tuankhai.travelassistants.utils.AppContansts;

/**
 * Created by Khai on 31/08/2017.
 */

public class BaseController {

    BaseActivity mActivity;
    BaseFragment curFragment = null;

    PlacesFragment placesFragment;
    SearchPlaceFragment searchFragment;

    public BaseController(BaseActivity activity) {
        mActivity = activity;
    }

    public void addPlaceFragment() {
        mActivity.searchView.clearQuery();
        if (placesFragment == null) {
            placesFragment = PlacesFragment.newInstance(mActivity);
        }
        curFragment = placesFragment;
        addFragment(curFragment, AppContansts.KEY_PLACE_FRAGMENT);
    }

    public void addSearchFragment() {
        if (searchFragment == null) {
            searchFragment = SearchPlaceFragment.newInstance(mActivity);
        }
        curFragment = searchFragment;
        addFragment(curFragment, AppContansts.KEY_SEARCH_FRAGMENT);
    }

    public void addFragment(BaseFragment fragment, String key){
        mActivity.fragmentManager = mActivity.getSupportFragmentManager();
        mActivity.fragmentTransaction = mActivity.fragmentManager.beginTransaction();
        mActivity.fragmentTransaction.replace(R.id.base_frame_content, fragment, key);
        mActivity.fragmentTransaction.commit();
    }

}
