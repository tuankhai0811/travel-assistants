package com.tuankhai.travelassistants.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

public class DetailPlaceActivity extends AppCompatActivity {
    PlaceDTO.Place data;
    Toolbar toolbar;
    SliderConfig mConfig;

    SliderImageAdapter adapterImage;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (PlaceDTO.Place) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        setContentView(R.layout.activity_detail_place);
        initCollapsingToolbar();
        initSlider();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        new GetImageSlider(data.arrImage).execute();
    }

    private void initSliderImage(ArrayList<Bitmap> arrImage) {
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        //indicator = (CircleIndicator) findViewById(R.id.indicatorImage);
        adapterImage = new SliderImageAdapter(this, arrImage);
        viewpager.setAdapter(adapterImage);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        //indicator.setViewPager(viewpager);
    }

    private void initCollapsingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSlider() {
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(android.R.color.transparent))
                .secondaryColor(getResources().getColor(android.R.color.transparent))
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
