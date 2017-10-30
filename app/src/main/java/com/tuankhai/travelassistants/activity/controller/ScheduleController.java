package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.ScheduleActivity;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.DeleteScheduleDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.CreateScheduleRequest;
import com.tuankhai.travelassistants.webservice.request.DeleteScheduleRequest;
import com.tuankhai.travelassistants.webservice.request.EditScheduleRequest;
import com.tuankhai.travelassistants.webservice.request.GetAllScheduleRequest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
                new GetAllScheduleRequest(mActivity.mUser.getEmail()),
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

    public void createNewSchedule(String name, Calendar fromDate, Calendar toDate) {
        new RequestService(mActivity).load(
                new CreateScheduleRequest(
                        mActivity.mUser.getEmail(),
                        name,
                        fromDate.getTimeInMillis() / 1000 + "",
                        toDate.getTimeInMillis() / 1000 + ""
                ),
                true,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AddScheduleDTO scheduleDTO = (AddScheduleDTO) response;
                        if (scheduleDTO.isSuccess()) {
                            mActivity.addSuccess(scheduleDTO.result);
                        } else {
                            mActivity.addFailure();
                        }
                    }

                    @Override
                    public void onFailure(Object error) {
                        super.onFailure(error);
                    }
                }, AddScheduleDTO.class);
    }

    public void deleteSchedule(final AddScheduleDTO.Schedule schedule) {
        new RequestService(mActivity).load(
                new DeleteScheduleRequest(schedule.id, mActivity.mUser.getEmail()),
                true,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        DeleteScheduleDTO scheduleDTO = (DeleteScheduleDTO) response;
                        if (scheduleDTO.isSuccess()) {
                            mActivity.deleteSuccess(schedule);
                        } else {
                            mActivity.deleteFailure();
                        }
                    }
                },
                DeleteScheduleDTO.class
        );
    }

    public void editSchedule(final AddScheduleDTO.Schedule schedule, String name, Calendar fromDate, Calendar toDate) {
        new RequestService(mActivity).load(
                new EditScheduleRequest(
                        schedule.id,
                        name,
                        mActivity.mUser.getEmail(),
                        fromDate.getTimeInMillis() / 1000 + "",
                        toDate.getTimeInMillis() / 1000 + ""
                ),
                true,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AddScheduleDTO scheduleDTO = (AddScheduleDTO) response;
                        if (scheduleDTO.isSuccess()){
                            mActivity.editScheduleSuccess(scheduleDTO);
                        } else {
                            mActivity.editScheduleFailure();
                        }
                    }
                },
                AddScheduleDTO.class
        );
    }
}
