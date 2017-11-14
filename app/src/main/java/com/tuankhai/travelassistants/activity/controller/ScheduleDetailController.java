package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.ScheduleDetailActivity;
import com.tuankhai.travelassistants.webservice.DTO.ScheduleDetailDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetScheduleDetailRequest;

/**
 * Created by Khai on 14/11/2017.
 */

public class ScheduleDetailController {
    ScheduleDetailActivity mActivity;

    public ScheduleDetailController(ScheduleDetailActivity activity) {
        this.mActivity = activity;
    }

    public void getDetailSchedule(String email, String id) {
        new RequestService(mActivity).load(
                new GetScheduleDetailRequest(email, id),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        ScheduleDetailDTO scheduleDetailDTO = (ScheduleDetailDTO) response;
                        if (scheduleDetailDTO.isSuccess()){
                            mActivity.getDetailSuccess(scheduleDetailDTO);
                        } else {
                            mActivity.getDetailFailure();
                        }
                    }
                },
                ScheduleDetailDTO.class
        );
    }
}
