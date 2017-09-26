package com.tuankhai.travelassistants.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tuankhai.slideractivity.Slider;
import com.tuankhai.slideractivity.model.SliderConfig;
import com.tuankhai.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.PlaceNearListAdapter;
import com.tuankhai.travelassistants.adapter.decoration.ListSpacingItemDecoration;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListPlaceNearActivity extends AppCompatActivity implements PlaceNearListAdapter.LayoutListPlaceNearItemListener
        , PlaceNearListAdapter.OnLoadMoreListener, SearchView.OnQueryTextListener {

    PlaceNearDTO data;
    String lat, lng;
    public Location location;
    int type;
    String title;

    SwipeRefreshLayout refreshLayout;
    RecyclerView lvPlace;
    List<PlaceNearDTO.Result> arrPlace;
    RecyclerView.LayoutManager layoutManager;
    PlaceNearListAdapter adapter;

    SearchView searchView;
    String stringQuery;

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
        addEvents();

        if (data != null) {
            refreshLayout.setRefreshing(true);
            if (type == AppContansts.INTENT_TYPE_FOOD) {
                title = getString(R.string.top_restaurent);
            } else {
                title = getString(R.string.top_hotel);
            }
            setTitle(title);
            List<PlaceNearDTO.Result> list = Arrays.asList(data.results);
            progressDistance(list);
            Collections.sort(list, PlaceNearDTO.Result.ComparatorDistance);
            arrPlace.addAll(list);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        } else {
            refreshLayout.setRefreshing(true);
            switch (type) {
                case AppContansts.INTENT_TYPE_ATM:
                    title = getString(R.string.top_atm);
                    break;
                case AppContansts.INTENT_TYPE_HOTEL:
                    title = getString(R.string.top_hotel);
                    break;
                case AppContansts.INTENT_TYPE_FOOD:
                    title = getString(R.string.top_restaurent);
                    break;
                case AppContansts.INTENT_TYPE_HOSPITAL:
                    title = getString(R.string.top_hospital);
                    break;
                case AppContansts.INTENT_TYPE_GAS_STATION:
                    title = getString(R.string.top_gas_station);
                    break;
                case AppContansts.INTENT_TYPE_DRINK:
                    title = getString(R.string.top_drinks);
                    break;
            }
            setTitle(title);
            new RequestService().nearPlace(type, lat, lng, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    data = (PlaceNearDTO) response;
                    if (data.results == null || data.results.length == 0) {
                        refreshLayout.setRefreshing(false);
                        findViewById(R.id.layout_notify).setVisibility(View.VISIBLE);
                        return;
                    }
                    List<PlaceNearDTO.Result> list = Arrays.asList(data.results);
                    progressDistance(list);
                    Collections.sort(list, PlaceNearDTO.Result.ComparatorDistance);
                    arrPlace.addAll(list);
                    adapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    super.onSuccess(response);
                }
            });
        }
    }

    private void addEvents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDistance(arrPlace);
                Collections.sort(arrPlace, PlaceNearDTO.Result.ComparatorDistance);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    public void progressDistance(ArrayList<PlaceNearDTO.Result> list) {
        for (PlaceNearDTO.Result item : list) {
            if (item.distance != 0) continue;
            Location mLocation = new Location("Place");
            mLocation.setLatitude(Double.parseDouble(item.geometry.location.lat));
            mLocation.setLongitude(Double.parseDouble(item.geometry.location.lng));
            item.distance = mLocation.distanceTo(location);
        }
    }

    public void progressDistance(List<PlaceNearDTO.Result> list) {
        for (PlaceNearDTO.Result item : list) {
            if (item.distance != 0) continue;
            Location mLocation = new Location("Place");
            mLocation.setLatitude(Double.parseDouble(item.geometry.location.lat));
            mLocation.setLongitude(Double.parseDouble(item.geometry.location.lng));
            item.distance = mLocation.distanceTo(location);
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
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        lvPlace = (RecyclerView) findViewById(R.id.lv_place);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvPlace.setLayoutManager(layoutManager);
        adapter = new PlaceNearListAdapter(this, lvPlace, arrPlace);
        lvPlace.addItemDecoration(new ListSpacingItemDecoration(Utils.dpToPx(this, 5)));
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
                        List<PlaceNearDTO.Result> list = Arrays.asList(data.results);
                        progressDistance(list);
                        Collections.sort(list, PlaceNearDTO.Result.ComparatorDistance);
                        arrPlace.addAll(list);
                        adapter.notifyItemInserted(index);
                        adapter.setLoaded();
                        if (!TextUtils.isEmpty(stringQuery)){
                            adapter.getFilter().filter(stringQuery);
                        }
                    }
                });
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_near_place_activity, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint(getString(R.string.action_search));
        searchView.setOnQueryTextListener(this);
//        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        stringQuery = newText;
        if (TextUtils.isEmpty(newText)) {
            adapter.clearTextFilter();
        } else {
            adapter.getFilter().filter(newText);
        }
        return true;
    }
}
