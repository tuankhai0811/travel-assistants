package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 02/11/2017.
 */

public class AddSchedulePlaceRequest extends BasicRequest {
    public final String URL = "SchedulePlace/add/new";

    private String email, id_schedule, name, id_place, date_start, date_end, description;

    public AddSchedulePlaceRequest(String email, String id_schedule, String name, String id_place, String date_start, String date_end, String description) {
        this.email = email;
        this.id_schedule = id_schedule;
        this.name = name;
        this.id_place = id_place;
        this.date_start = date_start;
        this.date_end = date_end;
        this.description = description;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("email", email);
        params.put("id_schedule", id_schedule);
        params.put("name", name);
        params.put("id_place", id_place);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("description", description);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}
