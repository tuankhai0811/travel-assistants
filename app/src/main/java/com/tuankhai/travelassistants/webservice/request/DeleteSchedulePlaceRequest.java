package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 05/11/2017.
 */

public class DeleteSchedulePlaceRequest extends BasicRequest {
    public final String URL = "SchedulePlace/delete/id";

    private String id, email;

    public DeleteSchedulePlaceRequest(String email, String id) {
        this.id = id;
        this.email = email;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("email", email);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}