package com.tuankhai.travelassistants.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ScheduleController;
import com.tuankhai.travelassistants.adapter.ScheduleAdapter;
import com.tuankhai.travelassistants.module.stickyadapter.StickyListHeadersListView;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener, AdapterView.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    ScheduleController mController;

    Toolbar toolbar;
    FloatingActionButton fab;
    SwipeRefreshLayout refreshLayout;

    //Rycycleview
    StickyListHeadersListView lvSchedule;
    ArrayList<AddScheduleDTO.Schedule> arrSchedule;
    ScheduleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        addControls();
        addEvents();
        mController.getData();
    }

    public void setData(List<AddScheduleDTO.Schedule> list) {
        Collections.sort(list);
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

        refreshLayout.setOnRefreshListener(this);
    }

    private void addControls() {
        mController = new ScheduleController(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        arrSchedule = new ArrayList<>();
        lvSchedule = (StickyListHeadersListView) findViewById(R.id.lv_schedule);
        lvSchedule.setOnItemClickListener(this);
        lvSchedule.setOnItemLongClickListener(this);
        lvSchedule.setOnHeaderClickListener(this);
        lvSchedule.setOnStickyHeaderChangedListener(this);
        lvSchedule.setOnStickyHeaderOffsetChangedListener(this);
//        lvSchedule.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
//        lvSchedule.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        lvSchedule.setEmptyView(findViewById(R.id.empty));
        lvSchedule.setDrawingListUnderStickyHeader(true);
        lvSchedule.setAreHeadersSticky(true);
        lvSchedule.setFastScrollEnabled(true);
//        lvSchedule.setFastScrollAlwaysVisible(true);
        lvSchedule.setStickyHeaderTopOffset(-20);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        //Độ trong suốt của header
        header.setAlpha(1);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }
}
