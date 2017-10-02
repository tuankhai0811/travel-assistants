package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 04/09/2017.
 */

public class FindPlaceByProvinceRequest extends BasicRequest {
    public final String ROUTES = "Place/find/province";

    private String key;
    private String id;

    public FindPlaceByProvinceRequest(String key, String id) {
        this.key = key;
        this.id = id;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("id", id);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
