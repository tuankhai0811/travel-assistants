package com.tuankhai.travelassistants.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.ProvinceAdapter;
import com.tuankhai.travelassistants.webservice.DTO.Province;

import java.util.ArrayList;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesFragment extends Fragment {
    protected String TAG = "";
    private View mRootView;
    protected PlacesController placesController;

    RecyclerView lvProvince;
    RecyclerView.LayoutManager layoutManagerProvince;
    ArrayList<Province> arrProvinces;
    ProvinceAdapter adapterProvinces;

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
    }
}
