package com.tuankhai.travelassistants.activity.controller;

import android.util.Log;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.MainActivity;
import com.tuankhai.travelassistants.fragment.BaseFragment;
import com.tuankhai.travelassistants.fragment.PlacesFragment;
import com.tuankhai.travelassistants.fragment.SearchPlaceFragment;
import com.tuankhai.travelassistants.utils.AppContansts;

/**
 * Created by Khai on 31/08/2017.
 */

public class MainController {

    private MainActivity mActivity;
    private BaseFragment curFragment = null;

    private PlacesFragment placesFragment;
    private SearchPlaceFragment searchFragment;

    public MainController(MainActivity activity) {
        mActivity = activity;
    }

    public void addPlaceFragment() {
        Log.e("status", "addPlaceFragment");
        mActivity.searchView.clearQuery();
        if (placesFragment == null) {
            Log.e("status", "new PlaceFragment");
            placesFragment = PlacesFragment.newInstance(mActivity);
        }
        curFragment = placesFragment;
        addFragment(curFragment, AppContansts.KEY_PLACE_FRAGMENT);
    }

    public void addSearchFragment() {
        Log.e("status", "addSearchFragment");
        if (searchFragment == null) {
            Log.e("status", "new PlaceFragment");
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
