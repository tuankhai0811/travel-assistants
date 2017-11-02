package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.SchedulePlaceController;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllSchedulePlaceDTO;

public class SchedulePlaceActivity extends BaseActivity {

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
//        schedule = (AddScheduleDTO.Schedule) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        mController.getDetail(getIntent().getStringExtra(AppContansts.INTENT_DATA));
        mController.getList(getIntent().getStringExtra(AppContansts.INTENT_DATA));
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

    public void getListSuccess(AllSchedulePlaceDTO allSchedulePlaceDTO) {

    }

    public void getListFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailSuccess(AddScheduleDTO addScheduleDTO) {
        schedule = addScheduleDTO.result;
        ((TextView) findViewById(R.id.txt_title)).setText(schedule.name);
    }
}
