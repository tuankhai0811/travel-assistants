package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 04/10/2017.
 */

public class QueryPlaceRequest extends BasicRequest {
    public final String ROUTES = "Place/find/query";

    private String key;
    private String query;

    public QueryPlaceRequest(String key, String query) {
        this.key = key;
        this.query = query;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("query", query);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}