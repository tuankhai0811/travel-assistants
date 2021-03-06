package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.ListPlaceController;
import com.tuankhai.travelassistants.adapter.PlaceAdapter;
import com.tuankhai.travelassistants.adapter.decoration.GridSpacingItemDecoration;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListPlaceActivity extends AppCompatActivity implements PlaceAdapter.LayoutListPlaceItemListener {
    ListPlaceController placeController;
    int type;

    ArrayList<PlaceDTO.Place> arrPlace;
    RecyclerView lvPlace;
    RecyclerView.LayoutManager layoutManager;
    PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeController = new ListPlaceController(this);
        setContentView(R.layout.activity_list_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getIntExtra(AppContansts.INTENT_TYPE, 0);
        init();
        switch (type) {
            case AppContansts.INTENT_TYPE_PROVINCE:
                progressProvince();
                break;
            case AppContansts.INTENT_TYPE_NORMAL:
                progressNormal();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void progressNormal() {
        int type = getIntent().getIntExtra(AppContansts.INTENT_DATA, 0);
        switch (type){
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
        placeAdapter = new PlaceAdapter(this, arrPlace, this);
        lvPlace.setLayoutManager(layoutManager);
        lvPlace.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dpToPx(this, 0), true));
        lvPlace.setItemAnimator(new DefaultItemAnimator());
        lvPlace.setAdapter(placeAdapter);
    }

    private void progressProvince() {
        ProvinceDTO.Province item = (ProvinceDTO.Province) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setTitle(item.name);
        placeController.getListPlace(item.id);
    }

    public void setListPlace(PlaceDTO response) {
        arrPlace.clear();
        arrPlace.addAll(Arrays.asList(response.place));
        placeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemPlaceClick(View view, PlaceDTO.Place item) {
        Intent intent = new Intent(ListPlaceActivity.this, DetailPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }
}
