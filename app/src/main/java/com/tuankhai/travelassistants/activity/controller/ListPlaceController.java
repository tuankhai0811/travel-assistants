package com.tuankhai.travelassistants.activity.controller;

import android.util.Log;

import com.tuankhai.travelassistants.activity.ListPlaceActivity;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.FindPlaceByProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.FindPlaceByTypeRequest;

/**
 * Created by tuank on 04/09/2017.
 */

public class ListPlaceController {
    ListPlaceActivity mActivity;

    public ListPlaceController(ListPlaceActivity activity) {
        mActivity = activity;
    }

    public void getListPlace(String id) {
        new RequestService().load(new FindPlaceByProvinceRequest("key", id), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response == null) {
                    onFailure("response is null");
                    return;
                }
                mActivity.setListPlace((PlaceDTO) response);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                Log.e("RequestService", error.toString());
            }
        }, PlaceDTO.class);
    }

    public void getList(int type) {
        new RequestService().load(new FindPlaceByTypeRequest("key", String.valueOf(type)), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response == null) {
                    onFailure("response is null");
                    return;
                }
                mActivity.setListPlace((PlaceDTO) response);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                Log.e("RequestService", error.toString());
            }
        }, PlaceDTO.class);
    }
}
