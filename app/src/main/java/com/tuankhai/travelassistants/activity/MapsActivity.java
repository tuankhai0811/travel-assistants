package com.tuankhai.travelassistants.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tuankhai.slideractivity.Slider;
import com.tuankhai.slideractivity.model.SliderConfig;
import com.tuankhai.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    int type;

    double location_lat, location_lng;
    String name;
    PlaceNearDTO data;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.BLACK);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        initSlider();

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
                addLocation(googleMap, Arrays.asList(data.results));
            }
            break;
            case AppContansts.INTENT_TYPE_NORMAL:
            default: {
                LatLng location = new LatLng(location_lat, location_lng);
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title(name)).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            }
            break;
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(this);
            return;
        }

//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                LatLng loc = new LatLng(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude());
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
//                return true;
//            }
//        });
    }

    private void addLocation(GoogleMap googleMap, List<PlaceNearDTO.Result> results) {
        for (PlaceNearDTO.Result item : results) {
            LatLng location = new LatLng(item.getLat(), item.getLng());
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(item.name)).showInfoWindow();
        }
        LatLng location = new LatLng(location_lat, location_lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14f));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is disabled!", Toast.LENGTH_SHORT).show();
        }
        Log.e("status", "MyLocation button clicked");
        return false;
    }
}
