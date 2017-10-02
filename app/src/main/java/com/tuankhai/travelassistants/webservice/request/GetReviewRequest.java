package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 13/09/2017.
 */

public class GetReviewRequest extends BasicRequest {
    public final String ROUTES = "Review/get/id";

    private String key;
    private String id_place;

    public GetReviewRequest(String key, String id_place) {
        this.key = key;
        this.id_place = id_place;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("id_place", id_place);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
