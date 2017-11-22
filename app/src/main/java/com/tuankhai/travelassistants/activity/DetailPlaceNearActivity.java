package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.ReviewsAdapter;
import com.tuankhai.travelassistants.adapter.SliderImageAdapter;
import com.tuankhai.travelassistants.library.loopingviewpager.CircleIndicator;
import com.tuankhai.travelassistants.library.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.library.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.library.viewpagertransformers.ZoomOutTranformer;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class DetailPlaceNearActivity extends BaseActivity
        implements View.OnClickListener, OnMapReadyCallback {

    private int type = AppContansts.TYPE_NORMAL;
    private PlaceNearDTO.Result data;
    private PlaceGoogleDTO.Result dataGoogle;
    private LatLng location;
    private Toolbar toolbar;

    //Slider Image
    private SliderImageAdapter adapterImage;
    private LoopViewPager viewpager;
    private CircleIndicator indicator;

    //Reviews
    private RecyclerView lvReview;
    private ArrayList<PlaceGoogleDTO.Result.Review> arrReview;
    private RecyclerView.LayoutManager layoutManagerReview;
    private ReviewsAdapter adapterReviews;

    //Progressbar review
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;

    private MaterialRatingBar ratingBar;
    private MaterialRatingBar ratingBarView;

    //Maps
    private MapView mapView;
    private GoogleMap mMap;

    //Swipe Image
    private int currentPage;
    private int numPage;
    private TimerTask task;
    private Timer timer;
    private final long DELAY_MS = 5000;      //delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000;    //time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.activity_detail_place_near);
        initSlider();
        getData();
        initCollapsingToolbar();
    }

    private void getData() {
        type = getIntent().getIntExtra(AppContansts.INTENT_TYPE, AppContansts.TYPE_NORMAL);
        if (type == AppContansts.TYPE_NORMAL) {
            data = (PlaceNearDTO.Result) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
            location = new LatLng(Double.parseDouble(data.getLat() + ""),
                    Double.parseDouble(data.getLng() + ""));
        } else {
            dataGoogle = (PlaceGoogleDTO.Result) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
            location = new LatLng(Double.parseDouble(dataGoogle.getLat() + ""),
                    Double.parseDouble(dataGoogle.getLng() + ""));
        }
        initStaticMaps();
        if (type == AppContansts.TYPE_NORMAL) {
            new RequestService().getPlace(data.place_id, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    super.onSuccess(response);
                    dataGoogle = ((PlaceGoogleDTO) response).result;
                    initProgressRatingbar();
                    addControlsPlaceGoogle();
                    initInformation();
                    initSliderImageGoogle();
                    initReviews();
                }
            });
        } else {
            initProgressRatingbar();
            addControlsPlaceGoogle();
            initInformation();
            initSliderImageGoogle();
            initReviews();
        }
    }

    private void initProgressRatingbar() {
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        ratingBar.invalidate();
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(Float.valueOf(dataGoogle.getRating()));

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
        ((TextView) findViewById(R.id.txt_address)).setText(dataGoogle.formatted_address);
        if (!Utils.isEmptyString(dataGoogle.formatted_phone_number)) {
            findViewById(R.id.layout_tel).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_tel)).setText(dataGoogle.formatted_phone_number);
            findViewById(R.id.txt_tel).setOnClickListener(this);
        }
        if (!Utils.isEmptyString(dataGoogle.website)) {
            findViewById(R.id.layout_website).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_website)).setText(dataGoogle.website);
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
        if (dataGoogle.reviews == null || dataGoogle.reviews.length == 0) return;
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
        adapterReviews = new ReviewsAdapter(this, arrReview, null);
        lvReview.setLayoutManager(layoutManagerReview);
        lvReview.setAdapter(adapterReviews);

        ((TextView) findViewById(R.id.num_comment)).setText(arrReview.size() + "");
        ((TextView) findViewById(R.id.txt_num_comment)).setText(arrReview.size() + " " + getString(R.string.txt_review));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        findViewById(R.id.layout_static_maps).setOnClickListener(this);
//        findViewById(R.id.layout_address).setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            timer.cancel();
            timer.purge();
            timer = null;
            task.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            viewpager.setOnPageChangeListener(null);
        }
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
            findViewById(R.id.layout_image_pager_detail_near).setBackgroundResource(R.drawable.bg_place_global_4_3);
        }
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        viewpager.setScrollDurationFactor(1000);
        adapterImage = new SliderImageAdapter(this, arrayImage);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = arrayImage.size();
        final Handler handler = new Handler();
        final Runnable update = new StaticRunnable(this);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ((TextView) findViewById(R.id.txt_title)).setText("");
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (type == AppContansts.TYPE_NORMAL) {
            collapsingToolbar.setTitle(data.name);
        } else {
            collapsingToolbar.setTitle(dataGoogle.name);
        }
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
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
                if (type == AppContansts.TYPE_NORMAL) {
                    collapsingToolbar.setTitle(data.name);
                } else {
                    collapsingToolbar.setTitle(dataGoogle.name);
                }
            }
        });
    }

    private void initSlider() {
        SliderConfig mConfig = new SliderConfig.Builder()
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataGoogle.website));
                startActivity(intent);
                break;
            case R.id.txt_tel:
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataGoogle.formatted_phone_number));
                startActivity(i);
                break;
        }
    }

    private static class StaticRunnable implements Runnable {
        private WeakReference weakReference;

        public StaticRunnable(BaseActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            DetailPlaceNearActivity activity = (DetailPlaceNearActivity) weakReference.get();
            if (activity != null) {
                activity.currentPage = activity.viewpager.getCurrentItem() + 1;
                if (activity.currentPage == activity.numPage) {
                    activity.currentPage = 0;
                }
                activity.viewpager.setCurrentItem(activity.currentPage--, true);
            }
        }
    }
}
