package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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

public class ListPlaceActivity extends AppCompatActivity {
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
        placeAdapter = new PlaceAdapter(this, arrPlace);
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
}
