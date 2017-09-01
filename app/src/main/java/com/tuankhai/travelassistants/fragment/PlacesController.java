package com.tuankhai.travelassistants.fragment;

import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesController {
    private PlacesFragment placesFragment;
    private BaseActivity mActivity;

    public PlacesController(PlacesFragment fragment) {
        placesFragment = fragment;
        mActivity = (BaseActivity) fragment.getActivity();
    }

    public void getAllProvince() {
        new RequestService().load(new GetListProvinceRequest(), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                placesFragment.setAllProvince((ProvinceDTO) response);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, ProvinceDTO.Province.class);
    }
}
