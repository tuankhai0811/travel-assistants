package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.ScheduleActivity;
import com.tuankhai.travelassistants.webservice.DTO.AllScheduleDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetAllScheduleRequest;

import java.util.Arrays;

/**
 * Created by tuank on 22/10/2017.
 */

public class ScheduleController {
    ScheduleActivity mActivity;

    public ScheduleController(ScheduleActivity activity) {
        this.mActivity = activity;
    }

    public void getData() {
        new RequestService().load(
                new GetAllScheduleRequest("tuankhai0811@gmail.com"),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AllScheduleDTO scheduleDTO = (AllScheduleDTO) response;
                        mActivity.setData(Arrays.asList(scheduleDTO.result));
                    }
                }, AllScheduleDTO.class);
    }
}
