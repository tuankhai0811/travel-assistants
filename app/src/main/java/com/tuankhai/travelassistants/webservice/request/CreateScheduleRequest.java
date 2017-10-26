package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 26/10/2017.
 */

public class CreateScheduleRequest extends BasicRequest {
    public final String ROUTES = "Schedule/add/new";

    private String email;
    private String name;
    private String date_start;
    private String date_end;

    public CreateScheduleRequest(String email, String name, String date_start, String date_end) {
        this.email = email;
        this.name = name;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        params.put("name", name);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}