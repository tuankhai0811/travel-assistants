package com.tuankhai.travelassistants.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.location.LocationHelper;
import com.tuankhai.travelassistants.module.slideractivity.Slider;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends BaseActivity
        implements AdapterView.OnItemSelectedListener, OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

    private int type;
    private double location_lat, location_lng;
    private String name;
    private PlaceNearDTO data;

    private GoogleMap mMap;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.BLACK);
//        }
        initToolbar();
        initSlider();

        addControls();

    }

    private void addControls() {
        locationHelper = new LocationHelper(this);

        type = getIntent().getIntExtra(AppContansts.INTENT_TYPE, 0);
        name = getIntent().getStringExtra(AppContansts.INTENT_NAME);
        data = (PlaceNearDTO) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        location_lat = Double.parseDouble(getIntent().getStringExtra(AppContansts.INTENT_DATA_LAT));
        location_lng = Double.parseDouble(getIntent().getStringExtra(AppContansts.INTENT_DATA_LNG));

        switch (type) {
            case AppContansts.INTENT_TYPE_ATM:
                name = getString(R.string.top_atm);
                break;
            case AppContansts.INTENT_TYPE_FOOD:
            case AppContansts.INTENT_TYPE_FOOD_GPS:
                name = getString(R.string.top_restaurent);
                break;
            case AppContansts.INTENT_TYPE_GAS_STATION:
                name = getString(R.string.top_gas_station);
                break;
            case AppContansts.INTENT_TYPE_HOTEL:
            case AppContansts.INTENT_TYPE_HOTEL_GPS:
                name = getString(R.string.top_hotel);
                break;
            case AppContansts.INTENT_TYPE_HOSPITAL:
                name = getString(R.string.top_hospital);
                break;
            case AppContansts.INTENT_TYPE_DRINK:
                name = getString(R.string.top_drinks);
                break;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
    }

    private void addEvents() {
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        switch (type) {
            case AppContansts.INTENT_TYPE_ATM:
            case AppContansts.INTENT_TYPE_FOOD:
            case AppContansts.INTENT_TYPE_FOOD_GPS:
            case AppContansts.INTENT_TYPE_GAS_STATION:
            case AppContansts.INTENT_TYPE_HOTEL:
            case AppContansts.INTENT_TYPE_HOTEL_GPS:
            case AppContansts.INTENT_TYPE_HOSPITAL:
            case AppContansts.INTENT_TYPE_DRINK: {
                addLocation(mMap, Arrays.asList(data.results));
            }
            break;
            case AppContansts.INTENT_TYPE_NORMAL:
            default: {
                LatLng location = new LatLng(location_lat, location_lng);
                mMap.addMarker(new MarkerOptions().position(location)
                        .title(name)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            }
            break;
        }
        LatLng location = new LatLng(location_lat, location_lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.setMaxZoomPreference(18.0f);
        mMap.setMinZoomPreference(13.0f);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14f));
        if (!(data == null || Utils.isEmptyString(data.next_page_token))) {
            getMoreData(data.next_page_token);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            locationHelper.checkpermission();
        }
    }

    private void getMoreData(String token) {
        new RequestService().nearPlace(type, String.valueOf(location_lat), String.valueOf(location_lng), token, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                PlaceNearDTO placeNearDTO = (PlaceNearDTO) response;
                addLocation(mMap, Arrays.asList(placeNearDTO.results));
                if (!Utils.isEmptyString(placeNearDTO.next_page_token)) {
                    getMoreData(placeNearDTO.next_page_token);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void addLocation(GoogleMap googleMap, List<PlaceNearDTO.Result> results) {
        for (PlaceNearDTO.Result item : results) {
            LatLng location = new LatLng(item.getLat(), item.getLng());
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(item.name));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
