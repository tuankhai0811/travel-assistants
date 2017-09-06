package com.tuankhai.travelassistants.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.slideractivity.Slider;
import com.tuankhai.slideractivity.model.SliderConfig;
import com.tuankhai.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.SliderImageAdapter;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.viewpagertransformers.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DetailPlaceActivity extends AppCompatActivity {
    PlaceDTO.Place data;
    Toolbar toolbar;
    SliderConfig mConfig;

    SliderImageAdapter adapterImage;
    LoopViewPager viewpager;
    CircleIndicator indicator;

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
        data = (PlaceDTO.Place) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setContentView(R.layout.activity_detail_place);
        initCollapsingToolbar();
        initSlider();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        initSliderImage(data.arrImage);
    }

    private void initSliderImage(ArrayList<String> array) {
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        viewpager.setScrollDurationFactor(1500);
        adapterImage = new SliderImageAdapter(this, array);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = array.size();
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
        ((TextView) findViewById(R.id.txt_title)).setText(data.getName());
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
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
                    collapsingToolbar.setTitle(data.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
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
                .distanceThreshold(0.4f)
                .edge(true)
                .edgeSize(0.4f)
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

//    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
//
//        ArrayList<String> lists;
//
//        public GetImageSlider(ArrayList<String> lists) {
//            this.lists = lists;
//        }
//
//        @Override
//        protected ArrayList<Bitmap> doInBackground(Void... voids) {
//            ArrayList<Bitmap> arrImg = new ArrayList<Bitmap>();
//            for (int i = 0; i < lists.size(); i++) {
//                try {
//                    URL url = new URL(lists.get(i));
//                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    arrImg.add(image);
//                } catch (IOException e) {
//                    System.out.println(e);
//                }
//            }
//            return arrImg;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
//            super.onPostExecute(bitmaps);
//            if (bitmaps.size() == 0) return;
//            initSliderImage(bitmaps);
//        }
//    }

}
