package com.tuankhai.travelassistants.fragment.controller;

import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.fragment.SearchResultFragment;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.SearchResultDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.QueryPlaceRequest;

/**
 * Created by Khai on 04/10/2017.
 */

public class ResultController {

    SearchResultFragment mFragment;
    BaseActivity mActivity;

    public ResultController(SearchResultFragment fragment){
        this.mFragment = fragment;
        this.mActivity = (BaseActivity) fragment.getActivity();
    }

    public void getResult(String query) {
        if (Utils.isEmptyString(query)) return;
        new RequestService().load(new QueryPlaceRequest("", query), false, new MyCallback(){
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                SearchResultDTO resultDTO = (SearchResultDTO) response;
                mFragment.refressData(resultDTO);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, SearchResultDTO.class);
    }
}
