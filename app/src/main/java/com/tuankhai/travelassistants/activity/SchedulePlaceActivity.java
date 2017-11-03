package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.SchedulePlaceController;
import com.tuankhai.travelassistants.adapter.SchedulePlaceAdapter;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.DetailScheduleDTO;

import java.util.ArrayList;
import java.util.Arrays;

public class SchedulePlaceActivity extends BaseActivity {

    SchedulePlaceController mController;

    Toolbar toolbar;
    RecyclerView lvSchedule;
    SchedulePlaceAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrSchedule;

    boolean flag = true;

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

        lvSchedule = (RecyclerView) findViewById(R.id.lv_schedule_place);
        arrSchedule = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new SchedulePlaceAdapter(this, arrSchedule);
        lvSchedule.setLayoutManager(layoutManager);
//        lvSchedule.addItemDecoration(new ListSpacingItemDecoration(Utils.dpToPx(this, 10)));
        lvSchedule.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            mController.getDetail(getIntent().getStringExtra(AppContansts.INTENT_DATA));
            mController.getList(getIntent().getStringExtra(AppContansts.INTENT_DATA));
            flag = false;
        }
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

        arrSchedule.addAll(Arrays.asList(allSchedulePlaceDTO.result));
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this, arrSchedule.size() + "", Toast.LENGTH_SHORT).show();
    }

    public void getListFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailSuccess(DetailScheduleDTO detailScheduleDTO) {
        schedule = detailScheduleDTO.result[0];
        ((TextView) findViewById(R.id.txt_title)).setText(schedule.name);
    }
}
