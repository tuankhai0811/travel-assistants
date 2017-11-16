package com.tuankhai.travelassistants.activity.controller;

import com.tuankhai.travelassistants.activity.ListPlaceScheduleActivity;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.DTO.DeleteScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.AddPlaceToScheduleDayRequest;
import com.tuankhai.travelassistants.webservice.request.DeletePlaceFromScheduleDayRequest;

/**
 * Created by Khai on 16/11/2017.
 */

public class ListPlaceScheduleController {
    ListPlaceScheduleActivity mActivity;

    public ListPlaceScheduleController(ListPlaceScheduleActivity activity){
        this.mActivity = activity;
    }

    public void addPlaceToSchedule(String email, String id_schedule, String id, final String id_place, int type) {
        new RequestService().load(
                new AddPlaceToScheduleDayRequest(email, id_schedule, id, id_place, type),
                false,
                new MyCallback(){
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        AddScheduleDayDTO addScheduleDayDTO = (AddScheduleDayDTO) response;
                        if (addScheduleDayDTO.isSuccess()){
                            mActivity.addPlace(addScheduleDayDTO);
                        }
                    }
                },
                AddScheduleDayDTO.class
        );
    }

    public void removePlaceFromSchedule(String email, String id_schedule, final String id, final String id_place, int type) {
        new RequestService().load(
                new DeletePlaceFromScheduleDayRequest(email, id_schedule, id, id_place, type),
                false,
                new MyCallback(){
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        DeleteScheduleDayDTO deleteScheduleDayDTO = (DeleteScheduleDayDTO) response;
                        if (deleteScheduleDayDTO.isSuccess()){
                            mActivity.removePlace(id_place);
                        }
                    }
                },
                DeleteScheduleDayDTO.class
        );
    }
}
