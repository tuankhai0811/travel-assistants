package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuankhai.travelassistants.module.slideractivity.Slider;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ListPlaceController;
import com.tuankhai.travelassistants.adapter.PlaceAdapter;
import com.tuankhai.travelassistants.adapter.PlaceAdapterSwipe;
import com.tuankhai.travelassistants.adapter.decoration.ListSpacingItemDecoration;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.tuankhai.travelassistants.utils.AppContansts.REQUEST_LOGIN;

public class ListPlaceActivity extends AppCompatActivity implements PlaceAdapter.LayoutListPlaceItemListener {

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    ListPlaceController placeController;
    public int type;

    ArrayList<PlaceDTO.Place> arrPlace;
    RecyclerView lvPlace;
    RecyclerView.LayoutManager layoutManager;
    PlaceAdapterSwipe placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        placeController = new ListPlaceController(this);

        setContentView(R.layout.activity_list_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSlider();

        type = getIntent().getIntExtra(AppContansts.INTENT_TYPE, 0);
        init();
    }

    private void initSlider() {
        SliderConfig mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.global_black))
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

    private void progressFavorite() {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
        setTitle(getString(R.string.title_favorite));
        placeController.getFavorite();
    }

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.txt_title)).setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                currentUser = mAuth.getCurrentUser();
                placeAdapter.setCurrentUser(currentUser);
                getData();
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void progressNormal() {
        int type = getIntent().getIntExtra(AppContansts.INTENT_DATA, 0);
        switch (type) {
            case AppContansts.INTENT_TYPE_SEA:
                setTitle(getResources().getString(R.string.type_sea));
                placeController.getList(AppContansts.INTENT_TYPE_SEA);
                break;
            case AppContansts.INTENT_TYPE_ATTRACTIONS:
                setTitle(getResources().getString(R.string.type_attractions));
                placeController.getList(AppContansts.INTENT_TYPE_ATTRACTIONS);
                break;
            case AppContansts.INTENT_TYPE_ENTERTAINMENT:
                setTitle(getResources().getString(R.string.type_entertainment));
                placeController.getList(AppContansts.INTENT_TYPE_ENTERTAINMENT);
                break;
            case AppContansts.INTENT_TYPE_CULTURAL:
                setTitle(getResources().getString(R.string.type_cultural));
                placeController.getList(AppContansts.INTENT_TYPE_CULTURAL);
                break;
            case AppContansts.INTENT_TYPE_SPRING:
                setTitle(getResources().getString(R.string.type_spring));
                placeController.getList(AppContansts.INTENT_TYPE_SPRING);
                break;
            case AppContansts.INTENT_TYPE_SUMMER:
                setTitle(getResources().getString(R.string.type_summer));
                placeController.getList(AppContansts.INTENT_TYPE_SUMMER);
                break;
            case AppContansts.INTENT_TYPE_AUTUMN:
                setTitle(getResources().getString(R.string.type_autumn));
                placeController.getList(AppContansts.INTENT_TYPE_AUTUMN);
                break;
            case AppContansts.INTENT_TYPE_WINNER:
                setTitle(getResources().getString(R.string.type_winner));
                placeController.getList(AppContansts.INTENT_TYPE_WINNER);
                break;
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

    private void init() {
        lvPlace = (RecyclerView) findViewById(R.id.lv_place);
        arrPlace = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        placeAdapter = new PlaceAdapterSwipe(this, arrPlace, this);
        lvPlace.setLayoutManager(layoutManager);
        lvPlace.addItemDecoration(new ListSpacingItemDecoration(Utils.dpToPx(this, 10)));
        lvPlace.setItemAnimator(new DefaultItemAnimator());
        lvPlace.setAdapter(placeAdapter);
    }

    private void getData() {
        switch (type) {
            case AppContansts.INTENT_TYPE_PROVINCE:
                progressProvince();
                break;
            case AppContansts.INTENT_TYPE_NORMAL:
                progressNormal();
                break;
            case AppContansts.INTENT_TYPE_FAVORITE:
                progressFavorite();
                break;
        }
    }

    private void progressProvince() {
        ProvinceDTO.Province item = (ProvinceDTO.Province) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setTitle(item.name);
        placeController.getListPlace(item.id);
    }

    public void setListPlace(PlaceDTO.Place[] response) {
        if (response == null || response.length == 0) {
            showNotifyNullList();
        } else {
            hideNotifyNullList();
        }
        arrPlace.clear();
        arrPlace.addAll(Arrays.asList(response));
        placeAdapter.notifyDataSetChanged();
    }

    public void showNotifyNullList() {
        findViewById(R.id.layout_notify).setVisibility(View.VISIBLE);
        switch (type) {
            case AppContansts.INTENT_TYPE_FAVORITE:
                ((TextView) findViewById(R.id.txt_notify)).setText(getString(R.string.no_favorite));
                break;
            case AppContansts.INTENT_TYPE_PROVINCE:
                ((TextView) findViewById(R.id.txt_notify)).setText(getString(R.string.no_list_in_province));
                break;
            default:
                ((TextView) findViewById(R.id.txt_notify)).setText(getString(R.string.no_list_in_province));
                break;
        }
    }

    public void hideNotifyNullList() {
        findViewById(R.id.layout_notify).setVisibility(View.GONE);
    }

    @Override
    public void onItemPlaceClick(View view, PlaceDTO.Place item) {
        Intent intent = new Intent(ListPlaceActivity.this, DetailPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }
}
