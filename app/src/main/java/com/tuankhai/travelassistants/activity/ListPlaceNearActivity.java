package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.PlaceNearListAdapter;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.ArrayList;
import java.util.Arrays;

public class ListPlaceNearActivity extends AppCompatActivity implements PlaceNearListAdapter.LayoutListPlaceNearItemListener
        , PlaceNearListAdapter.OnLoadMoreListener {

    PlaceNearDTO data;
    String lat, lng;
    int type;

    RecyclerView lvPlace;
    ArrayList<PlaceNearDTO.Result> arrPlace;
    RecyclerView.LayoutManager layoutManager;
    PlaceNearListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (PlaceNearDTO) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        lat = getIntent().getStringExtra(AppContansts.INTENT_DATA1);
        lng = getIntent().getStringExtra(AppContansts.INTENT_DATA2);
        type = getIntent().getIntExtra(AppContansts.INTENT_DATA3, 0);
        arrPlace = new ArrayList<>();
        arrPlace.addAll(Arrays.asList(data.results));
        setContentView(R.layout.activity_list_place_near);

        addControls();
    }

    private void addControls() {
        lvPlace = (RecyclerView) findViewById(R.id.lv_place);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvPlace.setLayoutManager(layoutManager);
        adapter = new PlaceNearListAdapter(this, lvPlace, arrPlace);
        lvPlace.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemPlaceNearClick(View view, PlaceNearDTO.Result item) {

    }

    @Override
    public void onLoadMore() {
        if (Utils.isEmptyString(data.next_page_token)) return;
        Log.e("status", "Load More");
        arrPlace.add(null);
        adapter.notifyItemInserted(arrPlace.size() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new RequestService().nearPlace(type, lat, lng, data.next_page_token, new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        data = (PlaceNearDTO) response;
                        arrPlace.remove(arrPlace.size() - 1);
                        int index = arrPlace.size();
                        arrPlace.addAll(Arrays.asList(data.results));
                        adapter.notifyItemInserted(index);
                        adapter.setLoaded();
                    }
                });
            }
        }, 1000);
    }
}
