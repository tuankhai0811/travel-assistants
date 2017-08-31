package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tuankhai.travelassistants.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseActivity extends FragmentActivity{
    protected String TAG = "";

    private BaseActivityCallback callback;
    private BaseController mBaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName().toString();
        mBaseController = new BaseController(this);
        setContentView(R.layout.activity_main);

        mBaseController.addPlacesFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setCallback(BaseActivityCallback callback) {
        this.callback = callback;
    }

    public interface BaseActivityCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
