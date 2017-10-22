package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 22/10/2017.
 */

public class DeleteScheduleRequest extends BasicRequest {
    public final String URL = "Schedule/delete/id";

    private String id, email;

    public DeleteScheduleRequest(String id, String email) {
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