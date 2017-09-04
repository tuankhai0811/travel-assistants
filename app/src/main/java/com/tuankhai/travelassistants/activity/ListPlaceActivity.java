package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

import java.util.ArrayList;
import java.util.Arrays;

public class ListPlaceActivity extends AppCompatActivity {
    ListPlaceController placeController;
    int type;

    ArrayList<PlaceDTO.Place> arrPlace;

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

    private void init() {
        arrPlace = new ArrayList<>();
    }

    private void progressProvince() {
        ProvinceDTO.Province item = (ProvinceDTO.Province) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setTitle(item.name);
        placeController.getListPlace(item.id);
    }

    public void setListPlace(PlaceDTO response) {
        arrPlace.clear();
        arrPlace.addAll(Arrays.asList(response.place));
    }
}
