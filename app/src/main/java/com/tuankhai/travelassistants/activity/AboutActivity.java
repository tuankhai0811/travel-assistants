package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.customTab.CustomTabActivityHelper;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initToolbar();
        initSlider();

        addControls();
        addEvents();
    }

    private void addControls() {
        ((TextView) findViewById(R.id.txt_thanks)).setLinkTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ((TextView) findViewById(R.id.txt_thanks)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addEvents() {
        findViewById(R.id.txt_email).setOnClickListener(this);
        findViewById(R.id.txt_facebook).setOnClickListener(this);
        findViewById(R.id.txt_github).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ((TextView) findViewById(R.id.txt_title)).setText(getString(R.string.txt_title_activity_about));
    }

    private void initSlider() {
        SliderConfig mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimary))
                .position(SliderPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.2f)
                .edge(true)
                .edgeSize(0.2f)
                .build();
        Slider.attach(this, mConfig);
    }

    private void mail() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:tuankhai0811@gmail.com"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(AboutActivity.this, getString(R.string.send_mail_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_email:
                mail();
                break;
            case R.id.txt_facebook:
                CustomTabActivityHelper.openCustomTab(this, Uri.parse("https://www.facebook.com/tkhai1995"));
                break;
            case R.id.txt_github:
                CustomTabActivityHelper.openCustomTab(this, Uri.parse("https://github.com/tuankhai0811"));
                break;
        }
    }
}
