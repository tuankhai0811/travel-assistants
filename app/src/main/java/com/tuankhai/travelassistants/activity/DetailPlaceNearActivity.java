package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.ReviewsAdapter;
import com.tuankhai.travelassistants.adapter.SliderImageAdapter;
import com.tuankhai.travelassistants.module.loopingviewpager.CircleIndicator;
import com.tuankhai.travelassistants.module.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.module.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.module.slideractivity.Slider;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.module.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.module.viewpagertransformers.ZoomOutTranformer;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DetailPlaceNearActivity extends AppCompatActivity
        implements View.OnClickListener, OnMapReadyCallback {

    PlaceNearDTO.Result data;
    PlaceGoogleDTO dataGoogle;
    LatLng location;
    Toolbar toolbar;
    SliderConfig mConfig;

    //Slider Image
    SliderImageAdapter adapterImage;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    //Reviews
    RecyclerView lvReview;
    ArrayList<PlaceGoogleDTO.Result.Review> arrReview;
    RecyclerView.LayoutManager layoutManagerReview;
    ReviewsAdapter adapterReviews;

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    ProgressBar progressBar4;
    ProgressBar progressBar5;

    MaterialRatingBar ratingBar;
    MaterialRatingBar ratingBarView;

    //Maps
    MapView mapView;
    GoogleMap mMap;

    int currentPage;
    int numPage;
    TimerTask task;
    Timer timer;
    final long DELAY_MS = 5000;      //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;    //time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.activity_detail_place_near);
        getData();
        initCollapsingToolbar();
        initSlider();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void getData() {
        data = (PlaceNearDTO.Result) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        Log.e("status", new Gson().toJson(data));
        location = new LatLng(Double.parseDouble(data.getLat() + ""),
                Double.parseDouble(data.getLng() + ""));
        initProgressRatingbar();
        initStaticMaps();
        new RequestService().getPlace(data.place_id, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                dataGoogle = (PlaceGoogleDTO) response;
                addControlsPlaceGoogle();
                initInformation();
                initSliderImageGoogle();
                initReviews();
            }
        });
    }

    private void initProgressRatingbar() {
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        ratingBar.invalidate();
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(Float.valueOf(data.rating));

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar_1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar_2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar_3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar_4);
        progressBar5 = (ProgressBar) findViewById(R.id.progressBar_5);

        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);
        progressBar4.setMax(100);
        progressBar5.setMax(100);
    }

    private void initInformation() {
        ((TextView) findViewById(R.id.txt_address)).setText(dataGoogle.result.formatted_address);
        if (!Utils.isEmptyString(dataGoogle.result.formatted_phone_number)) {
            findViewById(R.id.layout_tel).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_tel)).setText(dataGoogle.result.formatted_phone_number);
            findViewById(R.id.txt_tel).setOnClickListener(this);
        }
        if (!Utils.isEmptyString(dataGoogle.result.website)) {
            findViewById(R.id.layout_website).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_website)).setText(dataGoogle.result.website);
            findViewById(R.id.txt_website).setOnClickListener(this);
        }
        findViewById(R.id.layout_address).setOnClickListener(this);
    }

    private void initStaticMaps() {
        findViewById(R.id.layout_static_maps).setVisibility(View.VISIBLE);
        mapView = (MapView) findViewById(R.id.lite_listrow_map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    private void initReviews() {
        if (dataGoogle.result.reviews == null || dataGoogle.result.reviews.length == 0) return;
        findViewById(R.id.layout_content_reviews).setVisibility(View.VISIBLE);
        arrReview = new ArrayList<>();
        arrReview.addAll(dataGoogle.getReviews());
        refreshReview();
        refreshProgressBar();
    }

    public void refreshProgressBar() {
        int size = arrReview.size();
        for (int k = 1; k < 6; k++) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                if ((int) arrReview.get(i).getRating() == k) {
                    count++;
                }
            }
            switch (k) {
                case 1:
                    Double progress1 = (double) count / (double) size;
                    progressBar1.setProgress((int) (progress1 * 100));
                    break;
                case 2:
                    Double progress2 = (double) count / (double) size;
                    progressBar2.setProgress((int) (progress2 * 100));
                    break;
                case 3:
                    Double progress3 = (double) count / (double) size;
                    progressBar3.setProgress((int) (progress3 * 100));
                    break;
                case 4:
                    Double progress4 = (double) count / (double) size;
                    progressBar4.setProgress((int) (progress4 * 100));
                    break;
                case 5:
                    Double progress5 = (double) count / (double) size;
                    progressBar5.setProgress((int) (progress5 * 100));
                    break;
            }
        }
    }

    private void refreshReview() {
        lvReview = (RecyclerView) findViewById(R.id.lv_reviews);
        lvReview.setNestedScrollingEnabled(false);
        layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Collections.sort(arrReview);
        adapterReviews = new ReviewsAdapter(this, arrReview);
        lvReview.setLayoutManager(layoutManagerReview);
        lvReview.setAdapter(adapterReviews);

        ((TextView) findViewById(R.id.num_comment)).setText(arrReview.size() + "");
        ((TextView) findViewById(R.id.txt_num_comment)).setText(arrReview.size() + " " + getString(R.string.txt_review));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
//        findViewById(R.id.layout_static_maps).setOnClickListener(this);
//        findViewById(R.id.layout_address).setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void addControlsPlaceGoogle() {
        ((TextView) findViewById(R.id.txt_rating_view)).setText(dataGoogle.getRating() + "");
        ratingBarView = (MaterialRatingBar) findViewById(R.id.ratingBarView);
        ratingBarView.invalidate();
        ratingBarView.setMax(5);
        ratingBarView.setNumStars(5);
        ratingBarView.setStepSize(0.1f);
        ratingBarView.setRating(dataGoogle.getRating());
    }

    private void initSliderImageGoogle() {
        ArrayList<String> arrayImage = dataGoogle.getImage();
        if (arrayImage.size() == 0) {
            findViewById(R.id.layout_image_pager_detail_near).setBackgroundResource(R.drawable.bg_place_global_16_10);
        }
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        viewpager.setScrollDurationFactor(1500);
        adapterImage = new SliderImageAdapter(this, arrayImage);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = arrayImage.size();
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewpager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage++, true);
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        };
        timer = new Timer();
        timer.schedule(task, DELAY_MS);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    timer = new Timer();
                    if (task != null) {
                        task.cancel();
                        task = null;
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(update);
                            }
                        };
                    }
                    timer.schedule(task, DELAY_MS);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCollapsingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.txt_title)).setText("");
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(data.name);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(data.name);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(data.name);
                    isShow = false;
                }
            }
        });
    }

    private void initSlider() {
        mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimary))
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_website:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataGoogle.result.website));
                startActivity(intent);
                break;
            case R.id.txt_tel:
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataGoogle.result.formatted_phone_number));
                startActivity(i);
                break;
        }
    }
}
