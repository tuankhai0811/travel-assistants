package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tuankhai.slideractivity.Slider;
import com.tuankhai.slideractivity.model.SliderConfig;
import com.tuankhai.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.PlaceNearListAdapter;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListPlaceNearActivity extends AppCompatActivity implements PlaceNearListAdapter.LayoutListPlaceNearItemListener
        , PlaceNearListAdapter.OnLoadMoreListener {

    PlaceNearDTO data;
    String lat, lng;
    public Location location;
    int type;
    String title;

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
        location = new Location("Position");
        location.setLatitude(Double.parseDouble(lat));
        location.setLongitude(Double.parseDouble(lng));
        arrPlace = new ArrayList<>();

        setContentView(R.layout.activity_list_place_near);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSlider();
        initCollapsingToolbar();
        addControls();

        if (data != null) {
            if (type == AppContansts.INTENT_TYPE_FOOD) {
                title = getString(R.string.top_restaurent);
            } else {
                title = getString(R.string.top_hotel);
            }
            setTitle(title);
            arrPlace.addAll(Arrays.asList(data.results));
            adapter.notifyDataSetChanged();
        } else {
            switch (type) {
                case AppContansts.INTENT_TYPE_ATM:
                    title = getString(R.string.top_atm);
                    break;
                case AppContansts.INTENT_TYPE_HOTEL:
                    title = getString(R.string.top_hotel);
                    break;
                case AppContansts.INTENT_TYPE_RESTAURANT:
                    title = getString(R.string.top_restaurent);
                    break;
            }
            setTitle(title);
            new RequestService().nearPlace(type, lat, lng, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    data = (PlaceNearDTO) response;
                    arrPlace.addAll(Arrays.asList(data.results));
                    adapter.notifyDataSetChanged();
                    super.onSuccess(response);
                }
            });
        }
    }

    public void setTitle(String mTitle) {
        ((TextView) findViewById(R.id.txt_title)).setText(mTitle);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    private void initCollapsingToolbar() {
//        getSupportActionBar().setTitle(title);
//        final CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(title);
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        appBarLayout.setExpanded(true);

//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(title);
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(title);
//                    isShow = false;
//                }
//            }
//        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadMore() {
        if (Utils.isEmptyString(data.next_page_token)) return;
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                arrPlace.add(null);
                adapter.notifyItemInserted(arrPlace.size() - 1);
            }
        }, 0);
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
