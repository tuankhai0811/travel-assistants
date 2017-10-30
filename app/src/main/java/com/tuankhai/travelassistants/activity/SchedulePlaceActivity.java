package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.SchedulePlaceController;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

public class SchedulePlaceActivity extends AppCompatActivity {

    SchedulePlaceController mController;

    Toolbar toolbar;

    AddScheduleDTO.Schedule schedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_place);

        addControls();
        addEvents();
    }

    private void addEvents() {

    }

    private void addControls() {
        mController = new SchedulePlaceController(this);
        schedule = (AddScheduleDTO.Schedule) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        ((TextView) findViewById(R.id.txt_title)).setText(schedule.name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
