package com.tuankhai.travelassistants.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.base.BaseActivity;
import com.tuankhai.travelassistants.activity.controller.SchedulePlaceController;
import com.tuankhai.travelassistants.adapter.SchedulePlaceAdapter;
import com.tuankhai.travelassistants.adapter.decoration.ListSpacingItemDecoration;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.DetailScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class SchedulePlaceActivity extends BaseActivity implements SchedulePlaceAdapter.OnItemSchedulePlaceActionListener {

    private SchedulePlaceController mController;
    private Toolbar toolbar;
    private RecyclerView lvSchedule;
    private SchedulePlaceAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrSchedule;

    //Dialog add schedule
    private Dialog dialogSchedule;
    private Button btnCancelDialogSchedule, btnCreateDialogSchedule;
    private TextView txtFromDate, txtToDate, txtNote;
    private Calendar current = Calendar.getInstance();
    private Calendar fromDate = Calendar.getInstance();
    private Calendar toDate = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    //Dialog del
    private Dialog dialogDel;
    private Button btnCancelDel, btnDel;
    private TextView txtNameDel;

    //Get Data first
    boolean flag = true;

    private AddScheduleDTO.Schedule schedule;

    //Current schedulePlace select
    private AddSchedulePlaceDTO.SchedulePlace mSchedulePlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_place);

        addControls();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            mController.getDetail(getIntent().getStringExtra(AppContansts.INTENT_DATA));
            flag = false;
        }
        mController.getList(getIntent().getStringExtra(AppContansts.INTENT_DATA));
    }

    private void addEvents() {

    }

    private void addControls() {
        mController = new SchedulePlaceController(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        initSlider();

        lvSchedule = (RecyclerView) findViewById(R.id.lv_schedule_place);
        arrSchedule = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new SchedulePlaceAdapter(this, arrSchedule, this);
        lvSchedule.setLayoutManager(layoutManager);
        lvSchedule.addItemDecoration(new ListSpacingItemDecoration(Utils.dpToPx(this, 10)));
        lvSchedule.setAdapter(mAdapter);

        //Dialog edit
        dialogSchedule = new Dialog(this, Utils.getAnimDialog(this));
        dialogSchedule.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSchedule.setContentView(R.layout.content_dialog_edit_schedule_place);
        dialogSchedule.setCanceledOnTouchOutside(false);
        dialogSchedule.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancelDialogSchedule = dialogSchedule.findViewById(R.id.btn_cancel_schedule);
        btnCreateDialogSchedule = dialogSchedule.findViewById(R.id.btn_create_schedule);
        txtNote = dialogSchedule.findViewById(R.id.txt_note);
        txtFromDate = dialogSchedule.findViewById(R.id.txt_from_date);
        txtToDate = dialogSchedule.findViewById(R.id.txt_to_date);
        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
        txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
        btnCancelDialogSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSchedule.dismiss();
            }
        });

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtNote.getWindowToken(), 0);
                showDialogPickerFromDate();
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtNote.getWindowToken(), 0);
                showDialogPickerToDate();
            }
        });

        btnCreateDialogSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.editSchedule(
                        mUser.getEmail(),
                        mSchedulePlace.id_schedule,
                        mSchedulePlace.id_place,
                        mSchedulePlace.id,
                        fromDate.getTimeInMillis() / 1000 + "",
                        toDate.getTimeInMillis() / 1000 + "",
                        txtNote.getText().toString());
            }
        });

        //Dialog del
        dialogDel = new Dialog(this, Utils.getAnimDialog(this));
        dialogDel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDel.setContentView(R.layout.content_dialog_del_schedule);
        dialogDel.setCanceledOnTouchOutside(false);
        dialogDel.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnCancelDel = dialogDel.findViewById(R.id.btn_cancel_del);
        btnDel = dialogDel.findViewById(R.id.btn_delete);
        txtNameDel = dialogDel.findViewById(R.id.txt_name_del);

        btnCancelDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDel.dismiss();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.deleteSchedulePlace(mUser.getEmail(), mSchedulePlace.id);
            }
        });
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

    private void showDialogPickerToDate() {
        int mYear = toDate.get(Calendar.YEAR);
        int mMonth = toDate.get(Calendar.MONTH);
        int mDay = toDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.set(Calendar.YEAR, year);
                toDate.set(Calendar.MONTH, month);
                toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDate.set(Calendar.HOUR_OF_DAY, 23);
                toDate.set(Calendar.MINUTE, 59);
                toDate.set(Calendar.SECOND, 59);
                txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
                if (toDate.getTimeInMillis() < schedule.getStart().getTime() || toDate.getTimeInMillis() > schedule.getEnd().getTime()) {
                    txtToDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Không thể chọn ngày ngoài lịch trình đã chọn!");
                } else if (fromDate.getTimeInMillis() >= toDate.getTimeInMillis()) {
                    txtToDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu!");
                } else {
                    txtToDate.setError(null);
                }
            }
        }, mYear, mMonth, mDay);
        pickerDialog.setTitle("Đến ngày");
        pickerDialog.show();
    }

    private void showDialogPickerFromDate() {
        int mYear = fromDate.get(Calendar.YEAR);
        int mMonth = fromDate.get(Calendar.MONTH);
        int mDay = fromDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.set(Calendar.YEAR, year);
                fromDate.set(Calendar.MONTH, month);
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDate.set(Calendar.HOUR_OF_DAY, 0);
                fromDate.set(Calendar.MINUTE, 0);
                fromDate.set(Calendar.SECOND, 0);
                txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
                if (fromDate.getTimeInMillis() < schedule.getStart().getTime() || fromDate.getTimeInMillis() > schedule.getEnd().getTime()) {
                    txtFromDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Không thể chọn ngày ngoài lịch trình đã chọn!");
                } else {
                    txtFromDate.setError(null);
                    if (fromDate.getTimeInMillis() > toDate.getTimeInMillis()) {
                        toDate.set(Calendar.YEAR, year);
                        toDate.set(Calendar.MONTH, month);
                        toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        toDate.set(Calendar.HOUR_OF_DAY, 23);
                        toDate.set(Calendar.MINUTE, 59);
                        toDate.set(Calendar.SECOND, 59);
                        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
                    }
                }
            }
        }, mYear, mMonth, mDay);
        pickerDialog.setTitle("Từ ngày");
        pickerDialog.show();
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getListSuccess(AllSchedulePlaceDTO allSchedulePlaceDTO) {
        arrSchedule.clear();
        arrSchedule.addAll(Arrays.asList(allSchedulePlaceDTO.result));
        Collections.sort(arrSchedule);
        if (arrSchedule.size() > 0) {
            hideEmpty();
        } else {
            showEmpty();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showEmpty() {
        findViewById(R.id.empty).setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        findViewById(R.id.empty).setVisibility(View.GONE);
    }

    public void getListFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailFail() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu");
    }

    public void getDetailSuccess(DetailScheduleDTO detailScheduleDTO) {
        schedule = detailScheduleDTO.result[0];
        ((TextView) findViewById(R.id.txt_title)).setText("Lịch trình: " + schedule.name);
        ((TextView) findViewById(R.id.txt_time)).setText(
                simpleDateFormat.format(schedule.getStart())
                        + " đến " + simpleDateFormat.format(schedule.getEnd()));
    }

    @Override
    public void scheduleEdit(AddSchedulePlaceDTO.SchedulePlace schedulePlace) {
        mSchedulePlace = schedulePlace;
        txtFromDate.setError(null);
        txtToDate.setError(null);
        txtNote.setText(mSchedulePlace.description);
        fromDate.setTime(mSchedulePlace.getStart());
        toDate.setTime(mSchedulePlace.getEnd());
        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
        txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
        dialogSchedule.show();
    }

    @Override
    public void scheduleRemove(AddSchedulePlaceDTO.SchedulePlace schedulePlace) {
        mSchedulePlace = schedulePlace;
        txtNameDel.setText("Xóa lịch trình " + mSchedulePlace.name + "?");
        dialogDel.show();
    }

    public void editScheduleFailure() {
        Utils.showFaildToast(this, "Trùng thời gian với lịch trình khác");
    }


    public void editScheduleSuccess(AddSchedulePlaceDTO addSchedulePlaceDTO) {
        Utils.showSuccessToast(this, "Cập nhật thành công");
        int pos = arrSchedule.indexOf(mSchedulePlace);
        arrSchedule.remove(pos);
        arrSchedule.add(addSchedulePlaceDTO.result);
        Collections.sort(arrSchedule);
        mAdapter.notifyDataSetChanged();
        dialogSchedule.dismiss();
    }

    public void deleteSuccess() {
        Utils.showSuccessToast(this, "Xóa thành công");
        dialogDel.dismiss();
        arrSchedule.remove(mSchedulePlace);
        mAdapter.notifyDataSetChanged();
        if (arrSchedule.size() > 0) {
            hideEmpty();
        } else {
            showEmpty();
        }
    }

    public void deleteFailure() {
        Utils.showFaildToast(this, "Hành động thất bại");
    }
}
