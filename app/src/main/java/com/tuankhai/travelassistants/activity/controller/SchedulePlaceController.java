package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.SchedulePlaceActivity;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.AllSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.DetailScheduleDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.DeleteSchedulePlaceRequest;
import com.tuankhai.travelassistants.webservice.request.EditSchedulePlaceRequest;
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
                        if (allSchedulePlaceDTO.isSuccess()) {
                            mActivity.getListSuccess(allSchedulePlaceDTO);
                        } else {
                            mActivity.getListFail();
                        }
                    }
                },
                AllSchedulePlaceDTO.class);
    }

    public void getDetail(String id) {
        new RequestService(mActivity).load(
                new GetDetailScheduleRequest(mActivity.mUser.getEmail(), id),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        DetailScheduleDTO detailScheduleDTO = (DetailScheduleDTO) response;
                        if (detailScheduleDTO.isSuccess()) {
                            mActivity.getDetailSuccess(detailScheduleDTO);
                        } else {
                            mActivity.getDetailFail();
                        }
                    }
                },
                DetailScheduleDTO.class);
    }

    public void editSchedule(
            String email,
            String id_schedule,
            String id_place,
            String id,
            String fromDate,
            String toDate,
            String note) {
        new RequestService(mActivity).load(
                new EditSchedulePlaceRequest(email, id_schedule, id_place, id, fromDate, toDate, note),
                true,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AddSchedulePlaceDTO addSchedulePlaceDTO = (AddSchedulePlaceDTO) response;
                        if (addSchedulePlaceDTO.isSuccess()) {
                            mActivity.editScheduleSuccess(addSchedulePlaceDTO);
                        } else {
                            mActivity.editScheduleFailure();
                        }
                    }
                },
                AddSchedulePlaceDTO.class
        );
    }

    public void deleteSchedulePlace(String email, String id) {
        new RequestService(mActivity).load(
                new DeleteSchedulePlaceRequest(email, id),
                true,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AllSchedulePlaceDTO allSchedulePlaceDTO = (AllSchedulePlaceDTO) response;
                        if (allSchedulePlaceDTO.isSuccess()){
                            mActivity.deleteSuccess();
                        } else {
                            mActivity.deleteFailure();
                        }
                    }
                },
                AllSchedulePlaceDTO.class
        );
    }
}
