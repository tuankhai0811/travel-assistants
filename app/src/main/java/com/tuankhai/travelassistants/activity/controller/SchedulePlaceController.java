package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.SchedulePlaceActivity;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetDetailScheduleRequest;
import com.tuankhai.travelassistants.webservice.request.GetSchedulePlaceRequest;

/**
 * Created by Khai on 30/10/2017.
 */

public class SchedulePlaceController {
    SchedulePlaceActivity mActivity;

    public SchedulePlaceController(SchedulePlaceActivity activity) {
        this.mActivity = activity;
    }

    public void getList(String id) {
        new RequestService(mActivity).load(
                new GetSchedulePlaceRequest(mActivity.mUser.getEmail(), id),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AllSchedulePlaceDTO allSchedulePlaceDTO = (AllSchedulePlaceDTO) response;
                        if (allSchedulePlaceDTO.isSuccess()){
                            mActivity.getListSuccess(allSchedulePlaceDTO);
                        } else {
                            mActivity.getListFail();
                        }
                    }
                },
                AddScheduleDTO.class);
    }

    public void getDetail(String id) {
        new RequestService(mActivity).load(
                new GetDetailScheduleRequest(mActivity.mUser.getEmail(), id),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AddScheduleDTO addScheduleDTO = (AddScheduleDTO) response;
                        if (addScheduleDTO.isSuccess()){
                            mActivity.getDetailSuccess(addScheduleDTO);
                        } else {
                            mActivity.getDetailFail();
                        }
                    }
                },
                AddScheduleDTO.class);
    }
}
