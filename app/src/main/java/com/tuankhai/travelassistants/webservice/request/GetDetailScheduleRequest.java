package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 03/11/2017.
 */

public class GetDetailScheduleRequest extends BasicRequest {
    public final String URL = "Schedule/get/id";

    private String email, id;

    public GetDetailScheduleRequest(String email, String id) {
        this.email = email;
        this.id = id;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("email", email);
        params.put("id", id);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}