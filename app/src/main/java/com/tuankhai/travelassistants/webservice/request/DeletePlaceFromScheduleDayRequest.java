package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 16/11/2017.
 */

public class DeletePlaceFromScheduleDayRequest extends BasicRequest {
    public final String URL = "ScheduleDay/delete/id";

    private String email, id_schedule, id_schedule_place, place_id;
    private int type;

    public DeletePlaceFromScheduleDayRequest(String email, String id_schedule, String id_schedule_place, String place_id, int type) {
        this.email = email;
        this.id_schedule = id_schedule;
        this.id_schedule_place = id_schedule_place;
        this.place_id = place_id;
        this.type = type;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("email", email);
        params.put("id_schedule", id_schedule);
        params.put("id_schedule_place", id_schedule_place);
        params.put("place_id", place_id);
        params.put("type", type);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}
