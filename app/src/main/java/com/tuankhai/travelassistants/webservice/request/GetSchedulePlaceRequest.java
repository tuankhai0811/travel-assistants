package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 03/11/2017.
 */

public class GetSchedulePlaceRequest extends BasicRequest {
    public final String URL = "SchedulePlace/get/id_schedule";

    private String email, id_schedule;

    public GetSchedulePlaceRequest(String email, String id_schedule) {
        this.email = email;
        this.id_schedule = id_schedule;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("email", email);
        params.put("id_schedule", id_schedule);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}