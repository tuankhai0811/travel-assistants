package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ScheduleDetailController;
import com.tuankhai.travelassistants.adapter.PlaceScheduleDayAdapter;
import com.tuankhai.travelassistants.library.readmore.ReadMoreTextView;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.DTO.DetailPlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.GetScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.DTO.ScheduleDetailDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleDetailActivity extends BaseActivity implements PlaceScheduleDayAdapter.LayoutListPlaceNearItemListener {

    private ScheduleDetailController mController;
    private Toolbar toolbar;
    private SliderConfig mConfig;

    private DetailPlaceDTO placeDTO;
    private PlaceNearDTO placeNearRestaurentDTO, placeNearHotelDTO;
    private ScheduleDetailDTO.ScheduleDetail scheduleDetail;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private ReadMoreTextView txtDescription;

    private TabHost tabHost;
    private RecyclerView lvRes, lvHotel;
    private ArrayList<AddScheduleDayDTO.ScheduleDay> arrRes, arrHotel;
    private PlaceScheduleDayAdapter adapterRes, adapterHotel;

    private View btnAddRes, btnAddHotel;

    //Get Data detail first
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        addControls();
        addEvents();
    }

    private void getData() {
        mController.getDetailPlace(scheduleDetail.id_place);
        mController.getListPlaceRestaurent(
                mUser.getEmail(),
                scheduleDetail.id_schedule,
                scheduleDetail.id,
                AppContansts.TYPE_RESTAURENT_SCHEDULE_DAY);
        mController.getListPlaceHotel(
                mUser.getEmail(),
                scheduleDetail.id_schedule,
                scheduleDetail.id,
                AppContansts.TYPE_HOTEL_SCHEDULE_DAY);
    }

    private void initSlider() {
        mConfig = new SliderConfig.Builder()
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

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            mController.getDetailSchedule(mUser.getEmail(), getIntent().getStringExtra(AppContansts.INTENT_DATA));
        }
        if (scheduleDetail != null) {
            mController.getListPlaceRestaurent(
                    mUser.getEmail(),
                    scheduleDetail.id_schedule,
                    scheduleDetail.id,
                    AppContansts.TYPE_RESTAURENT_SCHEDULE_DAY);
            mController.getListPlaceHotel(
                    mUser.getEmail(),
                    scheduleDetail.id_schedule,
                    scheduleDetail.id,
                    AppContansts.TYPE_HOTEL_SCHEDULE_DAY);
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

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(tabHost);
            }
        });

        btnAddRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeNearRestaurentDTO != null) {
                    gotoListRes();
                } else {
                    if (placeDTO != null) {
                        mController.getListRestaurent(placeDTO);
                    }
                }
            }
        });

        btnAddHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeNearHotelDTO != null) {
                    gotoListHotel();
                } else {
                    if (placeDTO != null) {
                        mController.getListHotel(placeDTO);
                    }
                }
            }
        });
    }

    private void addControls() {
        mController = new ScheduleDetailController(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        initSlider();

        txtDescription = (ReadMoreTextView) findViewById(R.id.txt_description);

        initTabHost();

        btnAddRes = findViewById(R.id.btn_add_restaurent);
        btnAddHotel = findViewById(R.id.btn_add_hotel);

        lvRes = (RecyclerView) findViewById(R.id.lv_restaurent);
        lvHotel = (RecyclerView) findViewById(R.id.lv_hotel);
        arrRes = new ArrayList<>();
        arrHotel = new ArrayList<>();
        adapterRes = new PlaceScheduleDayAdapter(this, arrRes, this);
        adapterHotel = new PlaceScheduleDayAdapter(this, arrHotel, this);
        lvRes.setLayoutManager(new LinearLayoutManager(this));
        lvHotel.setLayoutManager(new LinearLayoutManager(this));
        lvRes.setAdapter(adapterRes);
        lvHotel.setAdapter(adapterHotel);
    }

    private void initTabHost() {
        tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec resSpec = tabHost.newTabSpec("restaurent");
        resSpec.setIndicator(getView(R.layout.tab_indicator_restaurent));
        resSpec.setContent(R.id.tab_restaurent);


        //Khởi tạo tab nghe nhạc
        TabHost.TabSpec hotelSpec = tabHost.newTabSpec("hotel");
        hotelSpec.setIndicator(getView(R.layout.tab_indicator_hotel));
        hotelSpec.setContent(R.id.tab_hotel);

        tabHost.addTab(resSpec);
        tabHost.addTab(hotelSpec);

        tabHost.setCurrentTab(0);
        setTabColor(tabHost);
    }

    public void setTabColor(TabHost tabhost) {
        int current = tabhost.getCurrentTab();
        TextView txtRes = tabhost.getTabWidget().getChildAt(0).findViewById(R.id.txt_tab_restaurent);
        TextView txtHotel = tabhost.getTabWidget().getChildAt(1).findViewById(R.id.txt_tab_hotel);
        if (current == 0) {
            txtRes.setTextColor(Color.WHITE);
            txtRes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            txtHotel.setTextColor(Color.BLACK);
            txtHotel.setBackgroundColor(Color.WHITE);
        } else {
            txtRes.setTextColor(Color.BLACK);
            txtRes.setBackgroundColor(Color.WHITE);
            txtHotel.setTextColor(Color.WHITE);
            txtHotel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void getDetailSuccess(ScheduleDetailDTO scheduleDetailDTO) {
        scheduleDetail = scheduleDetailDTO.result;
        getData();
        ((TextView) findViewById(R.id.txt_title)).setText("Lịch trình: " + scheduleDetail.name);
        ((TextView) findViewById(R.id.txt_time)).setText(
                "Lịch trình " + scheduleDetail.length + " ngày" + "\nTừ ngày " +
                        simpleDateFormat.format(scheduleDetail.getStart())
                        + " đến " + simpleDateFormat.format(scheduleDetail.getEnd()));

        if (!scheduleDetail.description.trim().isEmpty()) {
            txtDescription.setText(scheduleDetail.description);
        } else {
            txtDescription.setText("Không có ghi chú!");
        }
    }

    public void getDetailFailure() {
        Utils.showFaildToast(this, "Không lấy được dữ liệu!");
    }

    public void getDetailPlaceSuccess(DetailPlaceDTO placeDTO) {
        this.placeDTO = placeDTO;
    }

    public void getListRestaurentSuccess(PlaceNearDTO placeNearDTO) {
        placeNearRestaurentDTO = placeNearDTO;
        gotoListRes();
    }

    public void getListHotelSuccess(PlaceNearDTO placeNearDTO) {
        placeNearHotelDTO = placeNearDTO;
        gotoListHotel();
    }

    public void gotoListRes() {
        Intent intent = new Intent(this, ListPlaceScheduleActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, placeNearRestaurentDTO);
        intent.putExtra(AppContansts.INTENT_DATA1, placeDTO.place.getLocationLat());
        intent.putExtra(AppContansts.INTENT_DATA2, placeDTO.place.getLocationLng());
        intent.putExtra(AppContansts.INTENT_DATA3, AppContansts.TYPE_RESTAURENT_SCHEDULE_DAY);
        intent.putExtra(AppContansts.INTENT_DATA4, scheduleDetail);
        intent.putExtra(AppContansts.INTENT_DATA5, arrRes);
        startActivity(intent);
    }

    public void gotoListHotel() {
        Intent intent = new Intent(this, ListPlaceScheduleActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, placeNearHotelDTO);
        intent.putExtra(AppContansts.INTENT_DATA1, placeDTO.place.getLocationLat());
        intent.putExtra(AppContansts.INTENT_DATA2, placeDTO.place.getLocationLng());
        intent.putExtra(AppContansts.INTENT_DATA3, AppContansts.TYPE_HOTEL_SCHEDULE_DAY);
        intent.putExtra(AppContansts.INTENT_DATA4, scheduleDetail);
        intent.putExtra(AppContansts.INTENT_DATA5, arrHotel);
        startActivity(intent);
    }

    @Override
    public void onItemPlaceNearClick(View view, AddScheduleDayDTO.ScheduleDay item) {

    }

    public void getListPlaceRestaurentSuccess(GetScheduleDayDTO scheduleDayDTO) {
        arrRes.clear();
        arrRes.addAll(Arrays.asList(scheduleDayDTO.result));
        adapterRes.notifyDataSetChanged();
    }

    public void getListPlaceHotelSuccess(GetScheduleDayDTO scheduleDayDTO) {
        arrHotel.clear();
        arrHotel.addAll(Arrays.asList(scheduleDayDTO.result));
        adapterHotel.notifyDataSetChanged();
    }
}
