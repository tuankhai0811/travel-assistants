package com.tuankhai.travelassistants.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.ProvinceAdapter;
import com.tuankhai.travelassistants.adapter.SliderPlaceAdapter;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesFragment extends Fragment {
    protected String TAG = "";
    protected View mRootView;
    protected PlacesController placesController;

    RecyclerView lvProvince;
    RecyclerView.LayoutManager layoutManagerProvince;
    ArrayList<ProvinceDTO.Province> arrProvinces;
    ProvinceAdapter adapterProvinces;

    SliderPlaceAdapter adapterSliderPlace;
    ArrayList<Bitmap> arrImgSliderPlace;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    //auto swipe
    int currentPage;
    int numPage;
    Timer timer;
    final long DELAY_MS = 500;      //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000;    //time in milliseconds between successive task executions.

    public static PlacesFragment newInstance() {
        PlacesFragment fragment = new PlacesFragment();
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
            placesController = new PlacesController(this);
            placesController.getAllProvince();
            placesController.getSliderPlace();
        }
        //getChildFragmentManager().beginTransaction().replace(R.id.checkout_right_view, mRightFragment).commit();
        return mRootView;
    }

    private void addControls() {
        lvProvince = mRootView.findViewById(R.id.lv_province);
        arrProvinces = new ArrayList<>();
        layoutManagerProvince = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterProvinces = new ProvinceAdapter(getActivity(), arrProvinces);
        lvProvince.setLayoutManager(layoutManagerProvince);
        lvProvince.setAdapter(adapterProvinces);

//        viewpager = mRootView.findViewById(R.id.viewpager);
//        indicator = mRootView.findViewById(R.id.indicator);
//        arrImgSliderPlace = new ArrayList<>();
//        adapterSliderPlace = new SliderPlaceAdapter(getActivity(), arrImgSliderPlace);
//        viewpager.setAdapter(adapterSliderPlace);
//        indicator.setViewPager(viewpager);
    }

    public void setAllProvince(ProvinceDTO allProvince) {
        arrProvinces.clear();
        arrProvinces.addAll(Arrays.asList(allProvince.provinces));
        adapterProvinces.notifyDataSetChanged();
    }

    public void setSliderPlace(ArrayList<Bitmap> arrImg, SliderPlaceDTO.Place[] arrPlace) {
        viewpager = mRootView.findViewById(R.id.viewpagerPlace);
        indicator = mRootView.findViewById(R.id.indicatorPlace);
        arrImgSliderPlace = new ArrayList<>();
        arrImgSliderPlace.addAll(arrImg);
        adapterSliderPlace = new SliderPlaceAdapter(getActivity(), arrImgSliderPlace, arrPlace);
        viewpager.setAdapter(adapterSliderPlace);
        indicator.setViewPager(viewpager);
        currentPage = 0;
        numPage = arrImg.size();
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
        timer = new Timer();    //This will create a new Thread
        timer.schedule(new TimerTask() {    //task to be scheduled

            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}
