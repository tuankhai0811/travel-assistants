package com.tuankhai.travelassistants.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.customTab.CustomTabActivityHelper;
import com.tuankhai.travelassistants.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by tuank on 01/10/2017.
 */

public class BaseActivity extends AppCompatActivity {
    public static String mTAG = "";

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;

    public CustomTabActivityHelper mTabHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mTAG = getLocalClassName();

        mTabHelper = new CustomTabActivityHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTabHelper.bindCustomTabsService(this);
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
        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(this, "Vui lòng bật kết nối internet!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTabHelper.unbindCustomTabsService(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected View getView(int id) {
        return this.getLayoutInflater().inflate(id, null);
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

    public void goTo(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
