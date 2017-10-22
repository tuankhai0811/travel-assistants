package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ScheduleController;
import com.tuankhai.travelassistants.adapter.ScheduleAdapter;
import com.tuankhai.travelassistants.module.stickyadapter.StickyListHeadersListView;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {
    ScheduleController mController;

    Toolbar toolbar;
    FloatingActionButton fab;

    //Rycycleview
    StickyListHeadersListView lvSchedule;
    ArrayList<AddScheduleDTO.Schedule> arrSchedule;
    ScheduleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mController = new ScheduleController(this);

        Log.e("status", new Date().getTime() + "");

        addControls();
        addEvents();
        mController.getData();
    }

    public void setData(List<AddScheduleDTO.Schedule> list) {
        arrSchedule.clear();
        arrSchedule.addAll(list);
        mAdapter = new ScheduleAdapter(this, arrSchedule);
        lvSchedule.setAdapter(mAdapter);
    }

    private void addEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);

        arrSchedule = new ArrayList<>();
        lvSchedule = (StickyListHeadersListView) findViewById(R.id.lv_schedule);
        lvSchedule.setOnItemClickListener(this);
        lvSchedule.setOnHeaderClickListener(this);
        lvSchedule.setOnStickyHeaderChangedListener(this);
        lvSchedule.setOnStickyHeaderOffsetChangedListener(this);
//        lvSchedule.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
//        lvSchedule.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        lvSchedule.setEmptyView(findViewById(R.id.empty));
        lvSchedule.setDrawingListUnderStickyHeader(true);
        lvSchedule.setAreHeadersSticky(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }
}
