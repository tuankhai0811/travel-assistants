package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tuankhai.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.activity.ListPlaceActivity;
import com.tuankhai.travelassistants.adapter.ProvinceAdapter;
import com.tuankhai.travelassistants.adapter.SliderPlaceAdapter;
import com.tuankhai.travelassistants.fragment.controller.PlacesController;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.viewpagertransformers.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesFragment extends BaseFragment
        implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener,
        ProvinceAdapter.LayoutProvinceItemListener {
    protected BaseActivity mActivity;
    protected PlacesController placesController;
    BaseFragmentCallbacks callbacks;

    RecyclerView lvProvince;
    RecyclerView.LayoutManager layoutManagerProvince;
    ArrayList<ProvinceDTO.Province> arrProvinces;
    ProvinceAdapter adapterProvinces;

    SliderPlaceAdapter adapterSliderPlace;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    FrameLayout layoutSpring, layoutSummer, layoutAutumn, layoutWinter;
    LinearLayout layoutSea, layoutAttractions, layoutCultural, layoutEntertainment;

    //auto swipe
    int currentPage;
    int numPage;
    Timer timer;
    TimerTask task;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 5000;

    public static PlacesFragment newInstance(BaseActivity activity) {
        PlacesFragment fragment = new PlacesFragment();
        fragment.mActivity = activity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.TAG = "PlacesFragment";
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.places_activity, container, false);
            addControls();
            //addEvents();
            placesController = new PlacesController(this);
            placesController.getAllProvince();
            placesController.getSliderPlace();
        }
        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        addEvents();
    }

    private void addEvents() {
        layoutSpring.setOnClickListener(this);
        layoutSummer.setOnClickListener(this);
        layoutAutumn.setOnClickListener(this);
        layoutWinter.setOnClickListener(this);
        layoutSea.setOnClickListener(this);
        layoutAttractions.setOnClickListener(this);
        layoutEntertainment.setOnClickListener(this);
        layoutCultural.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_type_spring:
                layoutSpring.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_SPRING);
                break;

            case R.id.layout_type_summer:
                layoutSummer.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_SUMMER);
                break;

            case R.id.layout_type_autumn:
                layoutAutumn.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_AUTUMN);
                break;

            case R.id.layout_type_winter:
                layoutWinter.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_WINNER);
                break;

            case R.id.layout_type_sea:
                layoutSea.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_SEA);
                break;

            case R.id.layout_type_attractions:
                layoutAttractions.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_ATTRACTIONS);
                break;

            case R.id.layout_type_entertainment:
                layoutEntertainment.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_ENTERTAINMENT);
                break;

            case R.id.layout_type_cultural:
                layoutCultural.setOnClickListener(null);
                onTypeClick(view, AppContansts.INTENT_TYPE_CULTURAL);
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addControls() {
        attachSearchViewActivityDrawer(mActivity.searchView);
        mActivity.appBarLayout.addOnOffsetChangedListener(this);
        lvProvince = mRootView.findViewById(R.id.lv_province);
        arrProvinces = new ArrayList<>();
        layoutManagerProvince = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterProvinces = new ProvinceAdapter(mActivity, arrProvinces, this);
        lvProvince.setLayoutManager(layoutManagerProvince);
        lvProvince.setAdapter(adapterProvinces);

        layoutSpring = mRootView.findViewById(R.id.layout_type_spring);
        layoutSummer = mRootView.findViewById(R.id.layout_type_summer);
        layoutAutumn = mRootView.findViewById(R.id.layout_type_autumn);
        layoutWinter = mRootView.findViewById(R.id.layout_type_winter);

        layoutSea = mRootView.findViewById(R.id.layout_type_sea);
        layoutAttractions = mRootView.findViewById(R.id.layout_type_attractions);
        layoutCultural = mRootView.findViewById(R.id.layout_type_cultural);
        layoutEntertainment = mRootView.findViewById(R.id.layout_type_entertainment);
    }

    protected void attachSearchViewActivityDrawer(FloatingSearchView searchView) {
        if (callbacks != null) {
            callbacks.onAttachSearchViewToDrawer(searchView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragmentCallbacks) {
            callbacks = (BaseFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BaseFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public void setAllProvince(ProvinceDTO allProvince) {
        arrProvinces.clear();
        arrProvinces.addAll(Arrays.asList(allProvince.provinces));
        adapterProvinces.notifyDataSetChanged();
    }

    @Override
    public boolean onActivityBackPress() {
        if (mActivity.searchView.isSearchBarFocused()) {
            mActivity.searchView.clearSearchFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mActivity.searchView.setTranslationY(verticalOffset);
    }

    public void setSliderPlace(PlaceDTO data) {
        viewpager = mRootView.findViewById(R.id.viewpagerPlace);
        indicator = mRootView.findViewById(R.id.indicatorPlace);
        viewpager.setScrollDurationFactor(1500);
        adapterSliderPlace = new SliderPlaceAdapter(getActivity(), data);
        viewpager.setAdapter(adapterSliderPlace);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        indicator.setViewPager(viewpager);
        currentPage = 0;
        numPage = data.places.length;
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewpager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage--, true);
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

    @Override
    public void onProvinceClick(View view, ProvinceDTO.Province item) {
        ///////
        Intent intent = new Intent(mActivity, ListPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_PROVINCE);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }

    public void onTypeClick(View view, int type) {
        Intent intent = new Intent(mActivity, ListPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_NORMAL);
        intent.putExtra(AppContansts.INTENT_DATA, type);
        startActivity(intent);
    }

}
