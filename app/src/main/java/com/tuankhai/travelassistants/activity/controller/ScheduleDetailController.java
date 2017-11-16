package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.ScheduleDetailActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.DetailPlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.GetScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.DTO.ScheduleDetailDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetDetailPlaceRequest;
import com.tuankhai.travelassistants.webservice.request.GetListPlaceScheduleDayRequest;
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
                        if (scheduleDetailDTO.isSuccess()) {
                            mActivity.getDetailSuccess(scheduleDetailDTO);
                        } else {
                            mActivity.getDetailFailure();
                        }
                    }
                },
                ScheduleDetailDTO.class
        );
    }

    public void getDetailPlace(String id_place) {
        new RequestService().load(
                new GetDetailPlaceRequest(id_place),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        DetailPlaceDTO placeDTO = (DetailPlaceDTO) response;
                        if (placeDTO.isSuccess()) {
                            mActivity.getDetailPlaceSuccess(placeDTO);
                        }
                    }
                },
                DetailPlaceDTO.class
        );
    }

    public void getListRestaurent(final DetailPlaceDTO placeDTO) {
        new RequestService().nearPlace(
                AppContansts.INTENT_TYPE_FOOD,
                placeDTO.place.getLocationLat(),
                placeDTO.place.getLocationLng(),
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        PlaceNearDTO placeNearDTO = (PlaceNearDTO) response;
                        if (placeNearDTO.isSuccess()) {
                            mActivity.getListRestaurentSuccess(placeNearDTO);
                        }
                    }
                });
    }

    public void getListHotel(DetailPlaceDTO placeDTO) {
        new RequestService().nearPlace(
                AppContansts.INTENT_TYPE_HOTEL,
                placeDTO.place.getLocationLat(),
                placeDTO.place.getLocationLng(),
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        PlaceNearDTO placeNearDTO = (PlaceNearDTO) response;
                        if (placeNearDTO.isSuccess()) {
                            mActivity.getListHotelSuccess(placeNearDTO);
                        }
                    }
                });
    }

    public void getListPlaceRestaurent(String email, String id_schedule, String id, int type) {
        new RequestService().load(
                new GetListPlaceScheduleDayRequest(email, id_schedule, id, type),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        GetScheduleDayDTO scheduleDayDTO = (GetScheduleDayDTO) response;
                        if (scheduleDayDTO.isSuccess()) {
                            mActivity.getListPlaceRestaurentSuccess(scheduleDayDTO);
                        }
                    }
                },
                GetScheduleDayDTO.class
        );
    }

    public void getListPlaceHotel(String email, String id_schedule, String id, int type) {
        new RequestService().load(
                new GetListPlaceScheduleDayRequest(email, id_schedule, id, type),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        GetScheduleDayDTO scheduleDayDTO = (GetScheduleDayDTO) response;
                        if (scheduleDayDTO.isSuccess()) {
                            mActivity.getListPlaceHotelSuccess(scheduleDayDTO);
                        }
                    }
                },
                GetScheduleDayDTO.class
        );
    }
}
