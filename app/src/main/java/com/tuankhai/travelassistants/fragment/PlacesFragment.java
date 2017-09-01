package com.tuankhai.travelassistants.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;

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

    public void setSliderPlace(ArrayList<Bitmap> arrImg) {
        viewpager = mRootView.findViewById(R.id.viewpagerPlace);
        indicator = mRootView.findViewById(R.id.indicatorPlace);
        arrImgSliderPlace = new ArrayList<>();
        arrImgSliderPlace.addAll(arrImg);
        adapterSliderPlace = new SliderPlaceAdapter(getActivity(), arrImgSliderPlace);
        viewpager.setAdapter(adapterSliderPlace);
        indicator.setViewPager(viewpager);
//        arrImgSliderPlace.clear();
//        arrImgSliderPlace.addAll(arrImg);
//        //adapterSliderPlace.notifyDataSetChanged();
//        indicator.setViewPager(viewpager);
    }
}
