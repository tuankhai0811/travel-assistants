package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 07/09/2017.
 */

public class FindPlaceByTypeRequest extends BasicRequest{
    public final String ROUTES = "Place/find/type";

    private String key;
    private String type;

    public FindPlaceByTypeRequest(String key, String type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("type", type);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
