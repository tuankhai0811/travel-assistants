package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 13/11/2017.
 */

public class GetDetailPlaceRequest extends BasicRequest {
    public final String URL = "Place/get/detail";

    private String email, id;

    public GetDetailPlaceRequest(String id) {
        this.id = id;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}