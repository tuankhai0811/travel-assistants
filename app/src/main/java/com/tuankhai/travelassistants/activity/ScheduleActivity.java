package com.tuankhai.travelassistants.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ScheduleController;
import com.tuankhai.travelassistants.adapter.ScheduleAdapter;
import com.tuankhai.travelassistants.module.stickyadapter.StickyListHeadersListView;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ScheduleActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener, AdapterView.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    ScheduleController mController;

    Toolbar toolbar;
    //    FloatingActionButton fab;
    SwipeRefreshLayout refreshLayout;

    //Rycycleview
    StickyListHeadersListView lvSchedule;
    ArrayList<AddScheduleDTO.Schedule> arrSchedule;
    ScheduleAdapter mAdapter;

    //Dialog
    Dialog dialogAddNew;
    Button btnCancel, btnCreate;
    EditText txtName;
    TextView txtFromDate, txtToDate;
    Calendar current = Calendar.getInstance();
    Calendar fromDate = Calendar.getInstance();
    Calendar toDate = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        addControls();
        addEvents();
        mController.getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_schedule:
                dialogAddNew.show();
                txtName.setText("");
                txtFromDate.setText(simpleDateFormat.format(current.getTime()));
                txtToDate.setText(simpleDateFormat.format(current.getTime()));
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setData(List<AddScheduleDTO.Schedule> list) {
        Collections.sort(list);
        arrSchedule.clear();
        arrSchedule.addAll(list);
        mAdapter = new ScheduleAdapter(this, arrSchedule);
        lvSchedule.setAdapter(mAdapter);
    }

    private void addEvents() {
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogAddNew.show();
//            }
//        });
        refreshLayout.setOnRefreshListener(this);

        //Dialog
        btnCancel.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        txtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    txtName.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
    }

    private void addControls() {
        mController = new ScheduleController(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        fab = (FloatingActionButton) findViewById(fab);
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
        lvSchedule.setFastScrollEnabled(false);
        lvSchedule.setFastScrollAlwaysVisible(false);
        lvSchedule.setStickyHeaderTopOffset(-20);

        //Dialog
        dialogAddNew = new Dialog(this);
        dialogAddNew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddNew.setContentView(R.layout.content_dialog_new_schedule);
        dialogAddNew.setCanceledOnTouchOutside(false);
        dialogAddNew.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnCancel = dialogAddNew.findViewById(R.id.btn_cancel);
        btnCreate = dialogAddNew.findViewById(R.id.btn_create);
        txtName = dialogAddNew.findViewById(R.id.txt_name);
        txtFromDate = dialogAddNew.findViewById(R.id.txt_from_date);
        txtToDate = dialogAddNew.findViewById(R.id.txt_to_date);
        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
        txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dialogAddNew.dismiss();
                break;
            case R.id.btn_create:
                mController.createNewSchedule();
                dialogAddNew.dismiss();
                break;
            case R.id.txt_from_date:
                showDialogPickerFromDate();
                break;
            case R.id.txt_to_date:
                showDialogPickerToDate();
                break;
        }
    }

    private void showDialogPickerToDate() {
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.set(Calendar.YEAR, year);
                toDate.set(Calendar.MONTH, month);
                toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
            }
        }, year, month, day);
        pickerDialog.setTitle("Đến ngày");
        pickerDialog.show();
    }

    private void showDialogPickerFromDate() {
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.set(Calendar.YEAR, year);
                fromDate.set(Calendar.MONTH, month);
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
                txtFromDate.setError("Thời gian không đúng!");
                Utils.showFaildToast(ScheduleActivity.this, "Thời gian không đúng!");
            }
        }, year, month, day);
        pickerDialog.setTitle("Từ ngày");
        pickerDialog.show();
    }
}
