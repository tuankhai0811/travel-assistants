package com.tuankhai.travelassistants.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    double location_lat, location_lng;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        name = getIntent().getStringExtra(AppContansts.INTENT_NAME);
        location_lat = Double.parseDouble(getIntent().getStringExtra(AppContansts.INTENT_DATA_LAT));
        location_lng = Double.parseDouble(getIntent().getStringExtra(AppContansts.INTENT_DATA_LNG));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(location_lat, location_lng);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("aaaaaaaaaa"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
