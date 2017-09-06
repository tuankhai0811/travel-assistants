package com.tuankhai.travelassistants.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

import java.io.IOException;
import java.net.URL;
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
    ArrayList<Bitmap> arrImage;

    int currentPage;
    int numPage;
    TimerTask task;
    Timer timer;
    final long DELAY_MS = 3000;      //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;    //time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        data = (PlaceDTO.Place) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setContentView(R.layout.activity_detail_place);
        initCollapsingToolbar();
        initSlider();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        initSliderImage();
        new GetImageSlider(data.arrImage).execute();
    }

    private void initSliderImage() {
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        arrImage = new ArrayList<>();
//        //indicator = (CircleIndicator) findViewById(R.id.indicatorImage);
//        try {
//            Bitmap bmp = Glide.
//                    with(this).
//                    load(AppContansts.URL_IMAGE + data.id + AppContansts.IMAGE_RATIO_16_9).
//                    asBitmap().
//                    into(1280, 768).
//                    get();
//            arrImage.add(bmp);
//            adapterImage = new SliderImageAdapter(this, arrImage);
//            viewpager.setAdapter(adapterImage);
//            viewpager.setPageTransformer(true, new ZoomOutTranformer());
//            //indicator.setViewPager(viewpager);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

    }

    private void initSliderImage(ArrayList<Bitmap> array) {
        arrImage.clear();
        arrImage.addAll(array);
        adapterImage = new SliderImageAdapter(this, arrImage);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = arrImage.size();
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
        timer = new Timer();    //This will create a new Thread
        timer.schedule(task, DELAY_MS, PERIOD_MS);
    }

    private void initCollapsingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TextView)findViewById(R.id.txt_title)).setText(data.getName());
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
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
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
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

    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        ArrayList<String> lists;

        public GetImageSlider(ArrayList<String> lists) {
            this.lists = lists;
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {
            ArrayList<Bitmap> arrImg = new ArrayList<Bitmap>();
            for (int i = 0; i < lists.size(); i++) {
                try {
                    URL url = new URL(lists.get(i));
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    arrImg.add(image);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            return arrImg;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if (bitmaps.size() == 0) return;
            initSliderImage(bitmaps);
        }
    }

}
