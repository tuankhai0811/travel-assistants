package com.tuankhai.travelassistants.activity;

import android.support.v4.app.Fragment;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.fragment.PlacesFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Khai on 31/08/2017.
 */

public class BaseController {

    BaseActivity mActivity;
    Fragment curFragment = null;

    PlacesFragment placesFragment;

    public BaseController(BaseActivity activity) {
        mActivity = activity;
    }

    public void addPlaceFragment() {
        if (placesFragment == null)
            placesFragment = PlacesFragment.newInstance(mActivity);
        curFragment = placesFragment;
        mActivity.fragmentTransaction.replace(R.id.base_frame_content, curFragment);
        mActivity.fragmentTransaction.commit();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(mActivity.getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
