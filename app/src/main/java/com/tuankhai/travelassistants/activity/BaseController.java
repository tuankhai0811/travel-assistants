package com.tuankhai.travelassistants.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.fragment.PlacesFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Khai on 31/08/2017.
 */

public class BaseController {

    BaseActivity mActivity;
    Fragment fragment = null;

    public BaseController(BaseActivity activity) {
        mActivity = activity;
    }

    public void addPlacesFragment() {
        PlacesFragment placesFragment = PlacesFragment.newInstance();
        fragment = placesFragment;
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.base_frame_content, fragment);
        fragmentTransaction.commit();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(mActivity.getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
