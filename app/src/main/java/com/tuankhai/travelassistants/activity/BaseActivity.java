package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by tuank on 01/10/2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static String mTAG = "";

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mTAG = getLocalClassName();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mAuth.getCurrentUser();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_light))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void logError(String message) {
        Log.e(mTAG, message);
    }

    public void logError(Object object) {
        Log.e(mTAG, new Gson().toJson(object));
    }

    public void logError(String tag, String message) {
        Log.e(mTAG, tag + ": " + message);
    }

    public void logError(String tag, Object object) {
        Log.e(mTAG, tag + ": " + new Gson().toJson(object));
    }

}
