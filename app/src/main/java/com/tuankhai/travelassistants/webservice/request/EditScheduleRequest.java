package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 22/10/2017.
 */

public class EditScheduleRequest extends BasicRequest {
    public final String URL = "Schedule/edit/new";

    private String id, name, email, date_start, date_end;

    public EditScheduleRequest(String id, String name, String email, String date_start, String date_end) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("name", name);
        params.put("email", email);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}
