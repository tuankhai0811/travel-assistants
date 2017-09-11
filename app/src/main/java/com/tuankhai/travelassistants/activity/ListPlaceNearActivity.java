package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.PlaceNearListAdapter;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;

import java.util.Arrays;
import java.util.List;

public class ListPlaceNearActivity extends AppCompatActivity implements PlaceNearListAdapter.LayoutListPlaceNearItemListener {

    PlaceNearDTO data;

    RecyclerView lvPlace;
    List<PlaceNearDTO.Result> arrPlace;
    RecyclerView.LayoutManager layoutManager;
    PlaceNearListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (PlaceNearDTO) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        arrPlace = Arrays.asList(data.results);
        setContentView(R.layout.activity_list_place_near);

        addControls();
    }

    private void addControls() {
        lvPlace = (RecyclerView) findViewById(R.id.lv_place);
        adapter = new PlaceNearListAdapter(this, arrPlace, this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvPlace.setLayoutManager(layoutManager);
        lvPlace.setAdapter(adapter);
    }

    @Override
    public void onItemPlaceNearClick(View view, PlaceNearDTO.Result item) {

    }
}
