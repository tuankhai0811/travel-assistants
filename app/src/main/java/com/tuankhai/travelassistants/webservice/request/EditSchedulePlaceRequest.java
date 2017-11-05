package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 04/11/2017.
 */

public class EditSchedulePlaceRequest extends BasicRequest {
    public final String ROUTES = "SchedulePlace/edit/new";

    String email;
    String id_schedule;
    String id_place;
    String id;
    String date_start;
    String date_end;
    String description;

    public EditSchedulePlaceRequest(
            String email,
            String id_schedule,
            String id_place,
            String id,
            String date_start,
            String date_end,
            String description) {
        this.email = email;
        this.id_schedule = id_schedule;
        this.id_place = id_place;
        this.id = id;
        this.date_start = date_start;
        this.date_end = date_end;
        this.description = description;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("email", email);
        params.put("id_schedule", id_schedule);
        params.put("id_place", id_place);
        params.put("id", id);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("description", description);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
